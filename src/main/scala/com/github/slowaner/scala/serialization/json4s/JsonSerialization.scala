package com.github.slowaner.scala.serialization.json4s

import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.serialization
import com.github.slowaner.scala.serialization.json4s.predefined.PredefinedJson4sFormats
import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s
import org.json4s._
import org.json4s.ext.JodaTimeSerializers

/**
  * Created by gunman on 16.06.2017.
  */
trait JsonSerialization extends serialization.Serialization[JValue] with JsonDeserialization with PredefinedJson4sFormats {

  val serializers: Serializers[Any, JValue]
  val deserializers: Deserializers[JValue, Any]

  implicit val jsonFormats: Formats = DefaultFormats ++
    JodaTimeSerializers.all ++
    Set(
      Json2OffsetDateTimeSerializer,
      Json2LocalDateTimeSerializer,
      Json2ZonedDateTimeSerializer,
      Json2InstantSerializer,
    )

  val caseClassDeserializer = new JsonCaseClassDeserializer(this)

  override final def serializeSelf(a: Any): JValue =
    if (a == null) JNull
    else serializers.customSerializer.orElse(JsonSerializationImpl.serializers.customSerializer).applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def serializeAll(a: Any): JValue =
    if (a == null) JNull
    else serializers.customAllSerializer.orElse(JsonSerializationImpl.serializers.customAllSerializer).applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def serializeWith(a: Any, fieldsNames: Set[String]): JValue =
    if (a == null) JNull
    else serializers.customWithSerializer(fieldsNames).orElse(JsonSerializationImpl.serializers.customWithSerializer(fieldsNames)).applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def deserialize[R](a: json4s.JValue)(implicit ttag: ru.TypeTag[R]): R =
    deserializers.customDeserializer.orElse(JsonSerializationImpl.deserializers.customDeserializer).orElse(caseClassDeserializer.deserialize).applyOrElse((ttag, a),
      (tpl: (ru.TypeTag[_], JValue)) => tpl._2.extract(jsonFormats, Manifest.classType(tpl._1.mirror.runtimeClass(tpl._1.tpe)))).asInstanceOf[R]
}
