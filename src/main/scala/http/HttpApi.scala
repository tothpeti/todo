package http

import cats.syntax.all.*
import cats.effect.IO
import http.routes.{TestRoute, TodoRoute}
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.*
import service.TodoService

class HttpApi private (service: TodoService):
  private val versionV1 = "/v1"

  private val testRoutes: HttpRoutes[IO] = TestRoute().routes
  private val todoRoutes: HttpRoutes[IO] = TodoRoute.make(service).routes

  // Combining available routes
  private val openRoutes =
    testRoutes <+> todoRoutes

  private val routes = Router(
    versionV1 -> openRoutes
  )

  private val loggers: HttpApp[IO] => HttpApp[IO] = { (http: HttpApp[IO]) =>
    RequestLogger.httpApp(true, true)(http)
  } andThen { (http: HttpApp[IO]) =>
    ResponseLogger.httpApp(true, true)(http)
  }

  val httpApp: HttpApp[IO] = loggers(routes.orNotFound)

object HttpApi:
  def make(service: TodoService): IO[HttpApi] = IO(HttpApi(service))
