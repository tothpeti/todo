package domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}

import java.util.UUID

case class Todo(id: UUID, name: String, description: Option[String])

object Todo {
  implicit val todoDecoder: Decoder[Todo] = deriveDecoder
  implicit val todoEncoder: Encoder[Todo] = deriveEncoder
}
