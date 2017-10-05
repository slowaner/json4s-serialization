package org.slowaner.serialization.json

import org.json4s
import org.json4s.{CustomSerializer, _}

import java.text.DateFormat
import java.time._
import java.util.Date

import scala.reflect.runtime.{universe => ru}

import org.slowaner.serialization.{Deserializers, Serialization, Serializers}

/**
  * Created by gunman on 16.06.2017.
  */
trait JsonSerialization extends Serialization[JValue] with JsonDeserialization {

  val serializers: Serializers[Any, JValue] = jsonSerializers
  val deserializers: Deserializers[JValue, Any] = jsonDeserializers

  override def serializeSelf(a: Any): JValue = if (a == null) JNull else serializers.customSerializer.applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override def serializeAll(a: Any): JValue = if (a == null) JNull else serializers.customAllSerializer.applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override def serializeWith(a: Any, fieldsNames: Set[String]): JValue = if (a == null) JNull else serializers.customWithSerializer(fieldsNames).applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override def deserialize[R](a: json4s.JValue)(implicit ttag: ru.TypeTag[R]): R =
    deserializers.customDeserializer.orElse(JsonCaseClassDeserializer.deserialize).applyOrElse((ttag, a),
      (tpl: (ru.TypeTag[_], JValue)) => tpl._2.extract(jsonFormats, Manifest.classType(tpl._1.mirror.runtimeClass(tpl._1.tpe)))).asInstanceOf[R]
}

object JsonSerialization {

  object Json2DateSerializer extends CustomSerializer[Date](format => ( {
    case JString(dtmStr) => DateFormat.getDateTimeInstance.parse(dtmStr)
  }, {
    case dtm: Date =>
      JString(dtm.toString)
  }))

  object Json2LocalDateTimeSerializer extends CustomSerializer[LocalDateTime](format => ( {
    case JString(dtmStr) => LocalDateTime.parse(dtmStr)
  }, {
    case dtm: LocalDateTime => JString(dtm.toString)
  }))

  object Json2ZonedDateTimeSerializer extends CustomSerializer[ZonedDateTime](format => ( {
    case JString(dtmStr) => ZonedDateTime.parse(dtmStr)
  }, {
    case dtm: ZonedDateTime =>
      JString(dtm.toString)
  }))

  object Json2InstantSerializer extends CustomSerializer[Instant](format => ( {
    case JString(instStr) => Instant.parse(instStr)
  }, {
    case inst: Instant =>
      JString(inst.toString)
  }))

  object Json2OffsetDateTimeSerializer extends CustomSerializer[OffsetDateTime](format => ( {
    case JString(dtmStr) => OffsetDateTime.parse(dtmStr)
  }, {
    case dtm: OffsetDateTime =>
      JString(dtm.toString)
  }))
}
