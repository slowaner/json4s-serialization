package com.github.slowaner.scala.serialization.json4s

import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.serialization
import com.github.slowaner.scala.serialization.json4s.predefined.{PredefinedJson4sFormats, PredefinedSerializers}
import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s
import org.json4s._

import com.github.slowaner.scala.reflections4s.ReflectionHelper

/**
  * Created by slowaner on 16.06.2017.
  */
trait JsonSerialization extends serialization.Serialization[JValue] with JsonDeserialization with PredefinedJson4sFormats with PredefinedSerializers {
  def customSerializers: Serializers[Any, JValue]
  def customDeserializers: Deserializers[JValue, Any]
  def customJson4sFormats: Formats

  val serializers: Serializers[Any, JValue] = if (customSerializers != null) customSerializers ++ predefinedSerializers else predefinedSerializers
  val deserializers: Deserializers[JValue, Any] = if (customDeserializers != null) customDeserializers ++ predefinedDeserializers else predefinedDeserializers

  val jsonFormats: Formats = if (customJson4sFormats != null) customJson4sFormats ++ predefinedFormats.customSerializers else predefinedFormats

  override val jsonSerialization: JsonSerialization = this

  val caseClassDeserializer = new JsonCaseClassDeserializer(this)

  override final def serializeSelf(o: Any): JValue =
    if (serializers.customSerializer.isDefinedAt(o)) serializers.customSerializer(o)
    else Extraction.decompose(o)(jsonFormats)

  override final def serializeAll(o: Any): JValue =
    if (serializers.customAllSerializer.isDefinedAt(o)) serializers.customSerializer(o)
    else Extraction.decompose(o)(jsonFormats)

  override final def serializeWith(o: Any, fieldsNames: Set[String]): JValue =
    if (serializers.customWithSerializer(fieldsNames).isDefinedAt(o)) serializers.customWithSerializer(fieldsNames)(o)
    else Extraction.decompose(o)(jsonFormats)

  override final def deserialize[R](a: json4s.JValue)(implicit ttag: ru.TypeTag[R]): R = {
    val deser = deserializers.customDeserializer.orElse(caseClassDeserializer.deserialize)
    if (deser.isDefinedAt((ttag, a))) deser((ttag, a)).asInstanceOf[R]
    else a.extract(jsonFormats, ReflectionHelper.manifestFor(ttag))
  }
}
