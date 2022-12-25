import cats.syntax.all.*
import cats.effect.{IO, IOApp, Ref}
import cats.effect.implicits.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import resources.HttpServerConnection
import config.ConfigLoader
import domain.Todo
import http.HttpApi
import repository.InMemoryTodoRepository
import service.TodoService

import java.util.UUID

object Main extends IOApp.Simple:
  override def run: IO[Unit] = for {
    logger  <- Slf4jLogger.create[IO]
    _       <- logger.info("Starting Todo Application...")
    ref     <- Ref[IO].of(Map.empty[UUID, Todo])
    db       = InMemoryTodoRepository.make(ref, logger)
    service  = TodoService.make(db)
    config  <- config.ConfigLoader().load()
    httpApi <- HttpApi.make(service)
    _       <- HttpServerConnection.make(config.server, httpApi.httpApp, logger).useForever
  } yield ()
