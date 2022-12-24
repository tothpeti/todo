package http.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class TestRoute() extends Http4sDsl[IO] {
  private[routes] val prefixPath = "/test"

  private val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] { case GET -> Root =>
    Ok("hello world")
  }

  val routes: HttpRoutes[IO] = Router(
    prefixPath -> httpRoutes
  )
}
