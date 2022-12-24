package http

import cats.syntax.all.*
import cats.effect.IO
import http.routes.TestRoute
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.*

class HttpApi private ():
  private val versionV1 = "/v1"

  private val testRoutes: HttpRoutes[IO] = TestRoute().routes

  private val routes = Router(
    versionV1 -> testRoutes
  )

  private val loggers: HttpApp[IO] => HttpApp[IO] = { (http: HttpApp[IO]) =>
    RequestLogger.httpApp(true, true)(http)
  } andThen { (http: HttpApp[IO]) =>
    ResponseLogger.httpApp(true, true)(http)
  }

  val httpApp: HttpApp[IO] = loggers(routes.orNotFound)

object HttpApi:
  def make(): IO[HttpApi] = IO(HttpApi())
