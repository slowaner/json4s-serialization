package org.slowaner.serialization

import org.json4s.ext.JodaTimeSerializers
import org.json4s.{DefaultFormats, Formats, JValue, Serializer => Json4sSerializer}

import org.slowaner.serialization.json.JsonSerialization.{Json2InstantSerializer, Json2LocalDateTimeSerializer, Json2OffsetDateTimeSerializer, Json2ZonedDateTimeSerializer}

package object json {
  implicit val jsonFormats: Formats = DefaultFormats ++
    JodaTimeSerializers.all ++
    Set(
      Json2OffsetDateTimeSerializer,
      Json2LocalDateTimeSerializer,
      Json2ZonedDateTimeSerializer,
      Json2InstantSerializer,
    )

  implicit val jsonSerializers: Serializers[Any, JValue] = predefined.allSerializers

  implicit val jsonDeserializers: Deserializers[JValue, Any] = predefined.allDerializers
}
