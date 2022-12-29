import cats._
import cats.syntax.all._
import cats.effect._
import cats.effect.implicits._
import config.Config
import doobie.util.ExecutionContexts
import http.HttpApi
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import repository.DoobieTodoRepository
import resources.{Database, HttpServer}
import service.TodoService

object Main extends IOApp.Simple {

  private def resources =
    for {
      config     <- Resource.eval(Config.load())
      ec         <- ExecutionContexts.fixedThreadPool[IO](config.database.threadPoolSize)
      transactor <- Database.makeTransactor(config.database, ec)
    } yield (config, transactor)

  override def run: IO[Unit] =
    resources.use { case (config, xa) =>
      for {
        implicit0(logger: Logger[IO]) <- Slf4jLogger.create[IO]
        _                             <- logger.info("Starting application...")
        _                             <- Database.migrate(xa)
        db                             = DoobieTodoRepository.make(xa)
        service                        = TodoService.make(db)
        httpApi                        = HttpApi.make(service)
        _                             <- HttpServer.make(config.server, httpApi.httpApp).useForever
      } yield ()
    }

}
