package com.github.slowaner.scala.serialization.json4s.predefined

import java.text.DateFormat
import java.time.{Instant, LocalDateTime, OffsetDateTime, ZonedDateTime}
import java.util.Date

import org.json4s.ext.JodaTimeSerializers
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JString, JNull}

trait PredefinedJson4sFormats {

  val predefinedFormats: Formats = DefaultFormats ++
    JodaTimeSerializers.all ++
    Set(
      Json2OffsetDateTimeSerializer,
      Json2LocalDateTimeSerializer,
      Json2ZonedDateTimeSerializer,
      Json2InstantSerializer,
      Json2DateSerializer
    )

  object Json2DateSerializer extends CustomSerializer[Date](format => ( {
    case JString(dtmStr) => DateFormat.getDateTimeInstance.parse(dtmStr)
    case JNull => null
  }, {
    case dtm: Date => JString(dtm.toString)
  }))

  object Json2LocalDateTimeSerializer extends CustomSerializer[LocalDateTime](format => ( {
    case JString(dtmStr) => LocalDateTime.parse(dtmStr)
    case JNull => null
  }, {
    case dtm: LocalDateTime => JString(dtm.toString)
  }))

  object Json2ZonedDateTimeSerializer extends CustomSerializer[ZonedDateTime](format => ( {
    case JString(dtmStr) => ZonedDateTime.parse(dtmStr)
    case JNull => null
  }, {
    case dtm: ZonedDateTime => JString(dtm.toString)
  }))

  object Json2InstantSerializer extends CustomSerializer[Instant](format => ( {
    case JString(instStr) => Instant.parse(instStr)
    case JNull => null
  }, {
    case inst: Instant => JString(inst.toString)
  }))

  object Json2OffsetDateTimeSerializer extends CustomSerializer[OffsetDateTime](format => ( {
    case JString(dtmStr) => OffsetDateTime.parse(dtmStr)
    case JNull => null
  }, {
    case dtm: OffsetDateTime => JString(dtm.toString)
  }))

}
