package config

import cats.effect.IO
import pureconfig.*
import pureconfig.generic.derivation.default.*
import pureconfig.module.catseffect.syntax.*
import com.typesafe.config.ConfigFactory
import domain.{AppConfig, DbConfig, ServerConfig}

class ConfigLoader:

  given ConfigReader[ServerConfig] =
    ConfigReader.forProduct2("host", "port")(ServerConfig.apply)

  given ConfigReader[DbConfig] =
    ConfigReader.forProduct6("driver", "database", "url", "user", "password", "threadpoolsize")(DbConfig.apply _)

  given ConfigReader[AppConfig] =
    ConfigReader.forProduct2("server", "database")(AppConfig.apply)

  def load(configFile: String = "application.conf"): IO[AppConfig] =
    ConfigSource.fromConfig(ConfigFactory.load(configFile)).loadF[IO, AppConfig]()
