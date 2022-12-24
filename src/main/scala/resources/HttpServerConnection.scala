package resources

import cats.effect.{IO, Resource}
import org.http4s.server.Server
import domain.ServerConfig
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.defaults.Banner
import com.comcast.ip4s.{Host, Port, host, port}
import org.typelevel.log4cats.Logger

class HttpServerConnection private ()

object HttpServerConnection:
  def make(cfg: ServerConfig, httpApp: HttpApp[IO], logger: Logger[IO]): Resource[IO, Server] =
    val host = Host.fromString(cfg.host).getOrElse(host"0.0.0.0")
    val port = Port.fromInt(cfg.port).getOrElse(port"8080")

    EmberServerBuilder
      .default[IO]
      .withHost(host)
      .withPort(port)
      .withHttpApp(httpApp)
      .withLogger(logger)
      .build
      .evalTap(s => showEmberBanner(s, logger))

  private def showEmberBanner(s: Server, logger: Logger[IO]): IO[Unit] =
    logger.info(s"\n${Banner.mkString("\n")}\nHTTP Server started at ${s.address.getHostString}:${s.address.getPort}")
