package model

import java.util.UUID

case class Todo(
  id: UUID,
  name: String,
  description: String,
  isFinished: Boolean
)
