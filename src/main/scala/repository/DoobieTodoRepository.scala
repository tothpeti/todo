package repository

import doobie._
import doobie.implicits._
import doobie.hikari.HikariTransactor
import cats.effect.IO
import domain.Todo

import java.util.UUID

private object TodoSQL {
  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap[UUID](UUID.fromString)(_.toString)

  def select(id: UUID): Query0[Todo] =
    sql"""
          SELECT id, name, description
          FROM todo
          WHERE id = $id
      """.query[Todo]

  def select: ConnectionIO[List[Todo]] =
    sql"""
          SELECT id, name, description
          FROM todo
      """.query[Todo].to[List]

  def insertOrUpdate(newTodo: Todo): Update0 =
    sql"""
          INSERT INTO todo(id, name, description)
          VALUES (${newTodo.id}, ${newTodo.name}, ${newTodo.description})
          ON DUPLICATE KEY
          UPDATE name = ${newTodo.name}, description = ${newTodo.description}
      """.update

  def delete(id: UUID): Update0 =
    sql"""
          DELETE FROM todo
          WHERE id = $id
      """.update

  def delete: Update0 =
    sql"""
          DELETE FROM todo
      """.update

}

class DoobieTodoRepository private (transactor: HikariTransactor[IO]) extends TodoRepository {

  import TodoSQL._

  override def findAll(): IO[List[Todo]] =
    select.transact(transactor)

  override def findById(id: UUID): IO[Option[Todo]] =
    select(id).option.transact(transactor)

  override def upsert(newTodo: Todo): IO[Unit] =
    insertOrUpdate(newTodo)
      .withGeneratedKeys[String]("id")
      .compile
      .drain
      .transact(transactor)

  override def deleteById(id: UUID): IO[Unit] =
    delete(id)
      .withGeneratedKeys[String]("id")
      .compile
      .drain
      .transact(transactor)

  override def deleteAll(): IO[Unit] =
    delete
      .withGeneratedKeys[String]("id")
      .compile
      .drain
      .transact(transactor)

}

object DoobieTodoRepository {
  def make(transactor: HikariTransactor[IO]): DoobieTodoRepository = new DoobieTodoRepository(transactor)
}
