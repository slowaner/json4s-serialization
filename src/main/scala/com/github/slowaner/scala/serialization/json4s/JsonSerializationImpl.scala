package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization.json4s.predefined.PredefinedSerizalizers
import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s.JValue

object JsonSerializationImpl extends JsonSerialization with PredefinedSerizalizers {

  override val jsonSerialization: JsonSerialization = this

  override val serializers: Serializers[Any, JValue] = predefinedSerializers
  override val deserializers: Deserializers[JValue, Any] = predefinedDeserializers
}