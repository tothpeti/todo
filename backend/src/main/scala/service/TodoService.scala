package service

import repository.Repository


trait Service[F[_]] {

}

class TodoService[F[_]](repository: Repository[F]) extends Service[F] {

}
