package com.github.slowaner.scala.serialization.json4s.predefined

import scala.collection.JavaConverters._
import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.reflections4s.ReflectionHelper
import com.github.slowaner.scala.serialization.json4s._
import org.json4s._

trait PredefinedSerializers {

  val jsonSerialization: JsonSerialization

  val predefinedSerializers = new JsonSerializers[Any](
    Null2JsonSerializer.asInstanceOf[JsonSerializer[Any]],
    Seq2JsonSerializer.asInstanceOf[JsonSerializer[Any]],
    Set2JsonSerializer.asInstanceOf[JsonSerializer[Any]]
  )

  val predefinedDeserializers = new JsonDeserializers[Any](
    JValue2SetDeserializer,
    JValue2SeqDeserializer,
    JValue2MapDeserializer
  )


  //Serializers
  object Null2JsonSerializer extends JsonSerializer[Null] {
    override val serializeSelf: PartialFunction[Any, JValue] = {
      case null => JNull
    }

    override val serializeAll: PartialFunction[Any, JValue] = {
      case null => JNull
    }

    override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, JValue] = {
      case null => JNull
    }
  }

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

  // Deserializers
  object JValue2SetDeserializer extends JsonDeserializer[Set[_]] {
    val ttag: ru.TypeTag[Set[_]] = ru.typeTag[Set[_]]

    override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), Set[_]] = {
      case (jttag, JArray(elems)) if jttag.tpe <:< ttag.tpe => elems.map(jsonSerialization.deserialize(_)(ReflectionHelper.typeToTypeTag(jttag.tpe.typeArgs.head))).toSet
    }

  }

  object JValue2SeqDeserializer extends JsonDeserializer[Seq[_]] {
    val ttag: ru.TypeTag[Seq[_]] = ru.typeTag[Seq[_]]

    override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), Seq[_]] = {
      case (jttag, JArray(elems)) if jttag.tpe <:< ttag.tpe => elems.map(jsonSerialization.deserialize(_)(ReflectionHelper.typeToTypeTag(jttag.tpe.typeArgs.head)))
    }

  }

  object JValue2MapDeserializer extends JsonDeserializer[Map[_, _]] {
    val ttag: ru.TypeTag[Map[_, _]] = ru.typeTag[Map[_, _]]

    override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), Map[_, _]] = {
      case (jttag, JObject(elems)) if jttag.tpe <:< ttag.tpe => elems.map {
        case (name, jelem) =>
          jsonSerialization.deserialize(JString(name))(ReflectionHelper.typeToTypeTag(jttag.tpe.typeArgs.head))->
           jsonSerialization.deserialize(jelem)(ReflectionHelper.typeToTypeTag(jttag.tpe.typeArgs.head))
      }.toMap
    }

  }

}
