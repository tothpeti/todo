package service

import cats._
import cats.syntax.all._
import cats.effect.IO
import cats.effect.implicits._
import domain.Todo
import exception.TodoNotFoundException
import repository.TodoRepository

import java.util.UUID

class TodoService private (db: TodoRepository) {
  def findAll(): IO[List[Todo]] = db.findAll()

  def findById(id: UUID): IO[Either[TodoNotFoundException, Todo]] =
    db.findById(id).flatMap { todo =>
      IO.pure(Either.fromOption(todo, TodoNotFoundException(s"Todo with id: $id was not found.")))
    }

  def upsert(newTodo: Todo): IO[Unit] = db.upsert(newTodo)

  def deleteById(id: UUID): IO[Unit] = db.deleteById(id)

  def deleteAll(): IO[Unit] = db.deleteAll()

}

object TodoService {
  def make(db: TodoRepository) = new TodoService(db)
}
