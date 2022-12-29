package http.routes

import domain.Todo
import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import service.TodoService

class TodoRoute private (service: TodoService) extends Http4sDsl[IO] {
  private[routes] val prefixPath = "/todo"

  private val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(service.findAll())

    case GET -> Root / UUIDVar(uuid) =>
      service.findById(uuid).flatMap {
        case Right(value) => Ok(value)
        case Left(err)    => NotFound(err.msg)
      }

    case req @ POST -> Root =>
      req.decode[Todo](data => service.upsert(data).flatMap(Created(_)))

    case DELETE -> Root / UUIDVar(uuid) =>
      service.deleteById(uuid).flatMap(Ok(_))

    case DELETE -> Root =>
      service.deleteAll().flatMap(Ok(_))
  }

  val routes: HttpRoutes[IO] = Router(
    prefixPath -> httpRoutes
  )

}

object TodoRoute {
  def make(service: TodoService): TodoRoute = new TodoRoute(service)
}
