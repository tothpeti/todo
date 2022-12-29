package resources

import cats.effect.{IO, Resource}
import config.Config.DbConfig
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import scala.concurrent.ExecutionContext

object Database {
  def makeTransactor(cfg: DbConfig, ce: ExecutionContext): Resource[IO, HikariTransactor[IO]] =
    HikariTransactor.newHikariTransactor[IO](
      cfg.driver,
      cfg.url,
      cfg.user,
      cfg.password,
      ce
    )

  def migrate(transactor: HikariTransactor[IO]): IO[Unit] =
    transactor.configure { dataSource =>
      IO {
        val _: MigrateResult =
          Flyway
            .configure()
            .dataSource(dataSource)
            .load()
            .migrate()
      }.void
    }

}
