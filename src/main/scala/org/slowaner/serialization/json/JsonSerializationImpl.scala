package org.slowaner.serialization.json

import org.json4s.JValue

import org.slowaner.serialization.{Deserializers, Serializers}

object JsonSerializationImpl extends JsonSerialization {
  override val serializers: Serializers[Any, JValue] = predefined.predefinedSerializers
  override val deserializers: Deserializers[JValue, Any] = predefined.predefinedDeserializers
}