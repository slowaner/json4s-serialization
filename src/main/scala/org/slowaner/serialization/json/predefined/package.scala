package org.slowaner.serialization.json

import org.json4s.{JArray, JValue}

import scala.collection.JavaConverters._
import scala.reflect.runtime.{universe => ru}

package object predefined {
  // Serializers
  val Seq2JsonSerializer: JsonSerializer[Seq[Any]] = new JsonSerializer[Seq[Any]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(JsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(JsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(JsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }
  val Set2JsonSerializer: JsonSerializer[Set[_]] = new JsonSerializer[Set[_]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(JsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(JsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(JsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }
  val JavaSet2JsonSerializer: JsonSerializer[java.util.Set[_]] = new JsonSerializer[java.util.Set[_]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(JsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(JsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(JsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }

  val allSerializers = new JsonSerializers[Any](
    Seq2JsonSerializer.asInstanceOf[JsonSerializer[Any]],
    Set2JsonSerializer.asInstanceOf[JsonSerializer[Any]]
  )

  // Deserializers
  val allDerializers = new JsonDeserializers()
}
