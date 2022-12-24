package resources

import cats.effect.IO
import domain.AppConfig

class AppResources private (cfg: AppConfig)

object AppResources:
  def make(cfg: AppConfig): IO[AppResources] = IO(AppResources(cfg))

  private def checkDatabaseConnection = ???
  private def checkServerConnection   = ???
