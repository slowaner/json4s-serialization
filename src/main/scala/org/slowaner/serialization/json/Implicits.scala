package org.slowaner.serialization.json

import org.json4s.{DoubleMode => JsonDoubleMode, Implicits => JsonImplicits, _}

import java.time.{Instant, LocalDateTime, OffsetDateTime, ZonedDateTime}
import java.util.Date

import scala.reflect.runtime.{universe => ru}

trait Implicits extends JsonImplicits with JsonDoubleMode {
  // Serialization
  // Scala class serialization
  implicit def Set2Json(x: Set[_]): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaSet2Json(x: java.util.Set[_]): JValue = JsonSerializationImpl.serialize(x)

  // Java class serialization
  implicit def JavaInteger2Json(x: Integer): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaDate2Json(x: Date): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaLocalDateTime2Json(x: LocalDateTime): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaZonedDateTime2Json(x: ZonedDateTime): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaOffsetDateTime2Json(x: OffsetDateTime): JValue = JsonSerializationImpl.serialize(x)

  implicit def JavaInstant2Json(x: Instant): JValue = JsonSerializationImpl.serialize(x)


  implicit def Option2Json(x: Option[_]): JValue = x match {
    case Some(value) ⇒ JsonSerializationImpl.serialize(value)
    case _ ⇒ JNothing
  }

  // Common implicit deserialization
  implicit def JValue2Int(x: JValue): Int = JsonSerializationImpl.deserialize[Int](x)

  implicit def JValue2Double(x: JValue): Double = JsonSerializationImpl.deserialize[Double](x)

  implicit def JValue2Boolean(x: JValue): Boolean = JsonSerializationImpl.deserialize[Boolean](x)

  implicit def JValue2String(x: JValue): String = JsonSerializationImpl.deserialize[String](x)

  implicit def JValue2JavaLocalDateTime(x: JValue): LocalDateTime = JsonSerializationImpl.deserialize[LocalDateTime](x)

  implicit def JValue2JavaInteger(x: JValue): Integer = JsonSerializationImpl.deserialize[Integer](x)

  implicit def JValue2OptionString[R: ru.TypeTag](x: JValue): Option[R] = JsonSerializationImpl.deserialize[Option[R]](x)
}

object Implicits extends Implicits {

}
