package http

import cats.syntax.all._
import cats.effect.IO
import http.routes.{HealthRoute, TodoRoute}
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware._
import service.TodoService

class HttpApi private (service: TodoService) {
  private val versionV1 = "/v1"

  private val healthcheckRoutes: HttpRoutes[IO] = HealthRoute.make().routes
  private val todoRoutes: HttpRoutes[IO]        = TodoRoute.make(service).routes

  // Combining available routes
  private val openRoutes =
    healthcheckRoutes <+> todoRoutes

  private val routes = Router(
    versionV1 -> openRoutes
  )

  private val loggers: HttpApp[IO] => HttpApp[IO] = { (http: HttpApp[IO]) =>
    RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
  } andThen { (http: HttpApp[IO]) =>
    ResponseLogger.httpApp(logHeaders = true, logBody = true)(http)
  }

  val httpApp: HttpApp[IO] = loggers(routes.orNotFound)

}

object HttpApi {
  def make(service: TodoService): HttpApi = new HttpApi(service)
}
