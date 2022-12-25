package domain

import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.Decoder

import java.util.UUID

case class Todo(id: UUID, name: String, description: String)

object Todo:
  given Decoder[Todo] = deriveDecoder
  given Encoder[Todo] = deriveEncoder
