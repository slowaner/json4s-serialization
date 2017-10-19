package com.github.slowaner.scala.serialization.json4s

import java.time.LocalDateTime

import scala.language.implicitConversions
import scala.reflect.runtime.{universe => ru}

import org.json4s.{DoubleMode => JsonDoubleMode, Implicits => JsonImplicits, _}

trait Implicits extends JsonImplicits with JsonDoubleMode {

  val jsonSerialization: JsonSerialization

  // Serialization
  // Scala class serialization
  implicit def Set2Json(x: Set[_]): JValue = jsonSerialization.serialize(x)

  implicit def Int2Json(x: Int): JValue = jsonSerialization.serialize(x)

  implicit def Long2Json(x: Long): JValue = jsonSerialization.serialize(x)

  implicit def Option2Json(x: Option[_]): JValue = x match {
    case Some(value) ⇒ jsonSerialization.serialize(value)
    case _ ⇒ JNothing
  }

  // Java class serialization
  //
  implicit def JavaInteger2Json(x: java.lang.Integer): JValue = jsonSerialization.serialize(x)

  implicit def JavaSet2Json(x: java.util.Set[_]): JValue = jsonSerialization.serialize(x)

  implicit def JavaDate2Json(x: java.util.Date): JValue = jsonSerialization.serialize(x)

  implicit def JavaLocalDateTime2Json(x: java.time.LocalDateTime): JValue = jsonSerialization.serialize(x)

  implicit def JavaZonedDateTime2Json(x: java.time.ZonedDateTime): JValue = jsonSerialization.serialize(x)

  implicit def JavaOffsetDateTime2Json(x: java.time.OffsetDateTime): JValue = jsonSerialization.serialize(x)

  implicit def JavaInstant2Json(x: java.time.Instant): JValue = jsonSerialization.serialize(x)


  // Common implicit deserialization
  implicit def JValue2Int(x: JValue): Int = jsonSerialization.deserialize[Int](x)

  implicit def JValue2Long(x: JValue): Long = jsonSerialization.deserialize[Long](x)

  implicit def JValue2Double(x: JValue): Double = jsonSerialization.deserialize[Double](x)

  implicit def JValue2Boolean(x: JValue): Boolean = jsonSerialization.deserialize[Boolean](x)

  implicit def JValue2String(x: JValue): String = jsonSerialization.deserialize[String](x)

  implicit def JValue2Option[R: ru.TypeTag](x: JValue): Option[R] = jsonSerialization.deserialize[Option[R]](x)

  implicit def JValue2JavaLocalDateTime(x: JValue): java.time.LocalDateTime = jsonSerialization.deserialize[LocalDateTime](x)

  implicit def JValue2JavaInteger(x: JValue): java.lang.Integer = jsonSerialization.deserialize[Integer](x)
}
