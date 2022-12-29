package repository

import cats.effect.{IO, Ref}
import domain.Todo
import org.typelevel.log4cats.Logger

import java.util.UUID

class InMemoryTodoRepository private (ref: Ref[IO, Map[UUID, Todo]])(implicit logger: Logger[IO])
    extends TodoRepository {
  override def findAll(): IO[List[Todo]] = for {
    _  <- logger.info("Fetching all todos...")
    db <- ref.get
  } yield db.values.toList

  override def findById(id: UUID): IO[Option[Todo]] = for {
    _  <- logger.info(s"Fetching todo by id $id...")
    db <- ref.get
  } yield db.get(id)

  override def upsert(newTodo: Todo): IO[Unit] = for {
    _ <- logger.info(s"Upserting todo by id ${newTodo.id}...")
    _ <- ref.update(_ + (newTodo.id -> newTodo))
  } yield ()

  override def deleteById(id: UUID): IO[Unit] = for {
    _ <- logger.info(s"Deleting todo by id $id...")
    _ <- ref.update(_ - id)
  } yield ()

  override def deleteAll(): IO[Unit] = for {
    _ <- logger.info(s"Deleting all todo...")
  } yield ref.set(Map.empty[UUID, Todo])
}

object InMemoryTodoRepository {
  def make(db: Ref[IO, Map[UUID, Todo]])(implicit logger: Logger[IO]): InMemoryTodoRepository =
    new InMemoryTodoRepository(db)
}
