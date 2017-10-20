package com.github.slowaner.scala.serialization.json4s

import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.serialization
import com.github.slowaner.scala.serialization.json4s.predefined.{PredefinedJson4sFormats, PredefinedSerializers}
import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s
import org.json4s._

/**
  * Created by gunman on 16.06.2017.
  */
trait JsonSerialization extends serialization.Serialization[JValue] with JsonDeserialization with PredefinedJson4sFormats with PredefinedSerializers {
  val customSerializers: Serializers[Any, JValue]
  val customDeserializers: Deserializers[Any, JValue]
  val customJson4sFormats: Formats

  val serializers: Serializers[Any, JValue] = if (customSerializers != null) customSerializers ++ predefinedSerializers else predefinedSerializers
  val deserializers: Deserializers[JValue, Any] = if (customSerializers != null) customDeserializers ++ predefinedDeserializers else predefinedDeserializers

  val jsonFormats: Formats = if (customJson4sFormats != null) customJson4sFormats ++ predefinedFormats.customSerializers else predefinedFormats

  override val jsonSerialization: JsonSerialization = this

  val caseClassDeserializer = new JsonCaseClassDeserializer(this)

  override final def serializeSelf(a: Any): JValue =
    if (a == null) JNull
    else serializers.customSerializer.applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def serializeAll(a: Any): JValue =
    if (a == null) JNull
    else serializers.customAllSerializer.applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def serializeWith(a: Any, fieldsNames: Set[String]): JValue =
    if (a == null) JNull
    else serializers.customWithSerializer(fieldsNames).applyOrElse(a, (o: Any) => Extraction.decompose(o)(jsonFormats))

  override final def deserialize[R](a: json4s.JValue)(implicit ttag: ru.TypeTag[R]): R =
    deserializers.customDeserializer.orElse(caseClassDeserializer.deserialize).applyOrElse((ttag, a),
      (tpl: (ru.TypeTag[_], JValue)) => tpl._2.extract(jsonFormats, Manifest.classType(tpl._1.mirror.runtimeClass(tpl._1.tpe)))).asInstanceOf[R]
}
