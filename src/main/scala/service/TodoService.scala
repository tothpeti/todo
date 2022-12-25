package service

import cats.*
import cats.syntax.all.*
import cats.effect.IO
import cats.effect.implicits.*
import domain.Todo
import repository.TodoRepository

import java.util.UUID

class TodoService private (db: TodoRepository):
  def findAll(): IO[List[Todo]] = db.findAll()

  def findById(id: UUID): IO[Option[Todo]] = db.findById(id)

  def upsert(newTodo: Todo): IO[Unit] = db.upsert(newTodo)

  def deleteById(id: UUID): IO[Unit] = db.deleteById(id)

  def deleteAll(): IO[Unit] = db.deleteAll()

object TodoService:
  def make(db: TodoRepository) = TodoService(db)
