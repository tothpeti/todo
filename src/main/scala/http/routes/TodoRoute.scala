package http.routes

import domain.Todo
import cats.effect.IO
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import service.TodoService

class TodoRoute private (service: TodoService) extends Http4sDsl[IO]:
  private[routes] val prefixPath = "/todo"

  private val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(service.findAll())

    case GET -> Root / UUIDVar(uuid) =>
      service.findById(uuid).flatMap {
        case Some(todo) => Ok(todo)
        case None       => NotFound()
      }

    case req @ POST -> Root =>
      req.decode[Todo](data => service.upsert(data).flatMap(Ok(_)))

    case DELETE -> Root / UUIDVar(uuid) =>
      service.deleteById(uuid).flatMap(Ok(_))

    case DELETE -> Root =>
      service.deleteAll().flatMap(Ok(_))
  }

  val routes: HttpRoutes[IO] = Router(
    prefixPath -> httpRoutes
  )

object TodoRoute:
  def make(service: TodoService): TodoRoute = TodoRoute(service)
