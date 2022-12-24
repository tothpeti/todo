package domain

sealed trait Config extends Product with Serializable

final case class ServerConfig(host: String, port: Int) extends Config

final case class DbConfig(
  driver: String,
  database: String,
  url: String,
  user: String,
  password: String,
  threadPoolSize: Int
) extends Config

final case class AppConfig(server: ServerConfig, database: DbConfig) extends Config
