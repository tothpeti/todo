package repository

trait Repository[F[_]] {

}

class TodoRepository[F[_]]() extends Repository[F] {

}