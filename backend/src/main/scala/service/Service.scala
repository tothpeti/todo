package service

import exception.ServiceError
import model.Todo

import java.util.UUID

trait Service[F[_]] {
  def findById(id: UUID): F[Either[ServiceError, Todo]]

  def findAll(): F[Either[ServiceError, List[Todo]]]

  def save(newTodo: Todo): F[Unit]

  def deleteById(id: UUID): F[Unit]

  def deleteAll(): F[Unit]
}
