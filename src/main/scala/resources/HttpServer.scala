package resources

import cats.effect.{IO, Resource}
import org.http4s.server.Server
import org.http4s.{HttpApp, Response}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.defaults.Banner
import com.comcast.ip4s.{Host, IpLiteralSyntax, Port}
import config.Config.ServerConfig
import org.http4s.dsl.Http4sDsl
import org.typelevel.log4cats.Logger

object HttpServer {
  def make(cfg: ServerConfig, httpApp: HttpApp[IO])(implicit logger: Logger[IO]): Resource[IO, Server] = {
    val host = Host.fromString(cfg.host).getOrElse(host"0.0.0.0")
    val port = Port.fromInt(cfg.port).getOrElse(port"8080")

    EmberServerBuilder
      .default[IO]
      .withHost(host)
      .withPort(port)
      .withHttpApp(httpApp)
      .withLogger(logger)
      .withErrorHandler(errorHandler)
      .build
      .evalTap(s => showEmberBanner(s, logger))
  }

  private def errorHandler(implicit logger: Logger[IO]): PartialFunction[Throwable, IO[Response[IO]]] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    { case error =>
      logger.error(error)("Unexpected error happened while processing request") *>
        InternalServerError()
    }
  }

  private def showEmberBanner(s: Server, logger: Logger[IO]): IO[Unit] =
    logger.info(s"\n${Banner.mkString("\n")}\nHTTP Server started at ${s.address.getHostString}:${s.address.getPort}")
}
