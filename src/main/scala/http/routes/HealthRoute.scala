package http.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HealthRoute private () extends Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] { case GET -> Root / "health" =>
    Ok()
  }

}

object HealthRoute {
  def make(): HealthRoute = new HealthRoute()
}
