package repository
import model.Todo

import java.util.UUID

class TodoRepository[F[_]]() extends Repository[F] {
  override def findById(id: UUID): F[Option[Todo]] = ???

  override def findAll(): F[List[Todo]] = ???

  override def save(newTodo: Todo): F[Unit] = ???

  override def deleteById(id: UUID): F[Unit] = ???

  override def deleteAll(): F[Unit] = ???
}
