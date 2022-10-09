package repository

import model.Todo

import java.util.UUID

trait Repository[F[_]] {
  def findById(id: UUID): F[Option[Todo]]

  def findAll(): F[List[Todo]]

  def save(newTodo: Todo): F[Unit]

  def deleteById(id: UUID): F[Unit]

  def deleteAll(): F[Unit]
}
