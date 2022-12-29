package config

import cats._
import cats.syntax.all._
import cats.effect.IO
import cats.effect.implicits._
import com.typesafe.config.ConfigFactory
import pureconfig._
import pureconfig.module.catseffect.syntax._

object Config {
  final case class ServerConfig(host: String, port: Int)

  final case class DbConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    threadPoolSize: Int
  )

  final case class AppConfig(server: ServerConfig, database: DbConfig)

  implicit val serverConfigReader: ConfigReader[ServerConfig] =
    ConfigReader.forProduct2("host", "port")(ServerConfig.apply)

  implicit val dbConfigReader: ConfigReader[DbConfig] =
    ConfigReader.forProduct5("driver", "url", "user", "password", "threadpoolsize")(DbConfig.apply _)

  implicit val appConfigReader: ConfigReader[AppConfig] =
    ConfigReader.forProduct2("server", "database")(AppConfig.apply)

  def load(configFile: String = "application.conf"): IO[AppConfig] =
    ConfigSource.fromConfig(ConfigFactory.load(configFile)).loadF[IO, AppConfig]()
}
