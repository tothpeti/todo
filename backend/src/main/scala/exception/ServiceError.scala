package exception

sealed trait ServiceError extends Product with Serializable

final case class TodoNotFoundError(message: String)   extends ServiceError
final case class InternalServerError(message: String) extends ServiceError
