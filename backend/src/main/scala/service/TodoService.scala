package service

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import exception.{InternalServerError, ServiceError, TodoNotFoundError}
import model.Todo
import repository.Repository

import java.util.UUID

class TodoService[F[_]: Monad: Sync](repository: Repository[F]) extends Service[F] {
  override def findById(id: UUID): F[Either[ServiceError, Todo]] =
    for {
      todoOpt <- repository.findById(id)
    } yield todoOpt match {
      case Some(value) => Right(value)
      case None        => Left(TodoNotFoundError(s"Todo with $id was not found."))
    }

  override def findAll(): F[Either[ServiceError, List[Todo]]] =
    repository.findAll().attempt.map(_.leftMap(t => InternalServerError(t.getMessage)))

  // TODO add TodoAlreadyExistsException ... and add support for it which lookup the name of Todo if exists...
  override def save(newTodo: Todo): F[Unit] =
    repository.save(newTodo)

  // TODO add TodoNotFoundException ... in case of InMemoryRepository, this won't work, but in the real imlementation it will be handy
  override def deleteById(id: UUID): F[Unit] =
    repository.deleteById(id)

  override def deleteAll(): F[Unit] =
    repository.deleteAll()
}
