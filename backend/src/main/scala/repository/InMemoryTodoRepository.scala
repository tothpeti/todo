package repository

import cats.Monad
import cats.effect.{Ref, Sync}
import cats.implicits._
import model.Todo

import java.util.UUID

class InMemoryTodoRepository[F[_]: Monad: Sync](db: Ref[F, Map[UUID, Todo]]) extends Repository[F] {
  override def findById(id: UUID): F[Option[Todo]] =
    for {
      map <- db.get
    } yield map.get(id)

  override def findAll(): F[List[Todo]] =
    for {
      map <- db.get
    } yield map.values.toList

  override def save(newTodo: Todo): F[Unit] =
    for {
      _ <- db.update(_ + (newTodo.id -> newTodo))
    } yield ()

  override def deleteById(id: UUID): F[Unit] =
    for {
      _ <- db.update(_ - id)
    } yield ()

  override def deleteAll(): F[Unit] =
    for {
      _ <- db.set(Map.empty[UUID, Todo])
    } yield ()
}
