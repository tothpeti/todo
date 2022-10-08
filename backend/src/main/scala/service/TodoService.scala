package service

import model.Todo
import repository.Repository

import java.util.UUID

trait Service[F[_]] {
  def findById(id: UUID): F[Todo]

  def findAll(): F[List[Todo]]

  def save(newTodo: Todo): F[Unit]

  def deleteById(id: UUID): F[Unit]

  def deleteAll(): F[Unit]
}

class TodoService[F[_]](repository: Repository[F]) {}
