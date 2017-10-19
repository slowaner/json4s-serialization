package com.github.slowaner.scala.serialization.json4s.predefined

import scala.collection.JavaConverters._

import com.github.slowaner.scala.serialization.json4s.{JsonDeserializers, JsonSerialization, JsonSerializer, JsonSerializers}
import org.json4s.{JArray, JValue}

trait PredefinedSerializers {

  val jsonSerialization: JsonSerialization

  val predefinedSerializers = new JsonSerializers[Any](
    Seq2JsonSerializer.asInstanceOf[JsonSerializer[Any]],
    Set2JsonSerializer.asInstanceOf[JsonSerializer[Any]]
  )
  // Deserializers
  val predefinedDeserializers = new JsonDeserializers()

  object Seq2JsonSerializer extends JsonSerializer[Seq[Any]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(jsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(jsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case sq: Seq[Any] => JArray(sq.map(jsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }

  object Set2JsonSerializer extends JsonSerializer[Set[_]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(jsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(jsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case st: Set[_] => JArray(st.map(jsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }

  object JavaSet2JsonSerializer extends JsonSerializer[java.util.Set[_]] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(jsonSerialization.serializeSelf).toList)
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(jsonSerialization.serializeAll).toList)
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case st: java.util.Set[_] => JArray(st.asScala.map(jsonSerialization.serializeWith(_, fieldsNames)).toList)
    }
  }

}
