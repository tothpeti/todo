import cats.effect.{IO, IOApp, Ref}
import model.Todo
import repository.InMemoryTodoRepository
import service.TodoService

import java.util.UUID

object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    for {
      db     <- Ref.of[IO, Map[UUID, Todo]](Map.empty[UUID, Todo])
      repo    = new InMemoryTodoRepository[IO](db)
      service = new TodoService[IO](repo)
      id      = UUID.randomUUID()
      id2     = UUID.randomUUID()
      _      <- service.save(Todo(id, "asd", "blabla", isFinished = false))
      elem   <- service.findById(id)
      _      <- IO.delay(println(s"Find element: $elem"))
      _      <- service.save(Todo(id2, "asd2", "blabla2", isFinished = true))
      elems  <- service.findAll()
      _      <- IO.delay(println(s"Find elements: $elems"))
      _      <- service.deleteById(id)
      elems2 <- service.findAll()
      _      <- IO.delay(println(s"Find elements: $elems2"))
      _      <- service.save(Todo(id, "asd", "blabla", isFinished = false))
      _      <- service.deleteAll()
      elems3 <- service.findAll()
      _      <- IO.delay(println(s"Find elements: $elems3"))
    } yield ()
}
