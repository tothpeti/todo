import cats.implicits.*
import cats.effect.{IO, IOApp}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import resources.HttpServerConnection
import config.ConfigLoader
import http.HttpApi

object Main extends IOApp.Simple:
  override def run: IO[Unit] = for {
    logger  <- Slf4jLogger.create[IO]
    _       <- logger.info("Starting Todo Application...")
    loader   = config.ConfigLoader()
    config  <- loader.load()
    httpApi <- HttpApi.make()
    _       <- HttpServerConnection.make(config.server, httpApi.httpApp, logger).useForever
  } yield ()
