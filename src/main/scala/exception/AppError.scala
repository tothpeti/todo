package exception

sealed trait AppError extends Exception with Product with Serializable

final case class TodoNotFoundException(msg: String) extends AppError
