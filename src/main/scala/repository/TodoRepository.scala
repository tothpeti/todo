package repository

import cats.effect.IO
import domain.Todo

import java.util.UUID

trait TodoRepository {
  def findAll(): IO[List[Todo]]

  def findById(id: UUID): IO[Option[Todo]]

  def upsert(newTodo: Todo): IO[Unit]

  def deleteById(id: UUID): IO[Unit]

  def deleteAll(): IO[Unit]
}
