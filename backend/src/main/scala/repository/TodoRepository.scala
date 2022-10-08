package repository

import cats.Monad
import cats.implicits._
import cats.effect.Ref
import cats.effect.kernel.Sync
import model.Todo

import java.util.UUID

trait Repository[F[_]] {
  def findById(id: UUID): F[Todo]

  def findAll(): F[List[Todo]]

  def save(newTodo: Todo): F[Unit]

  def deleteById(id: UUID): F[Unit]

  def deleteAll(): F[Unit]
}

class TodoRepository[F[_]]() {}

class InMemoryTodoRepository[F[_]: Monad: Sync](db: Ref[F, Map[UUID, Todo]]) extends Repository[F] {
  override def findById(id: UUID): F[Todo] = ???

  override def findAll(): F[List[Todo]] = ???

  override def save(newTodo: Todo): F[Unit] = ???

  override def deleteById(id: UUID): F[Unit] = ???

  override def deleteAll(): F[Unit] = ???
}
