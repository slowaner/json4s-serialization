package com.github.slowaner.scala.serialization.json4s

import scala.reflect.runtime.{universe => ru}

import org.json4s.JValue

class JsonCustomDeserializer[R: ru.TypeTag](deserializer: PartialFunction[JValue, R]) extends JsonDeserializer[R] {
  override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), R] = {
    case (`ttag`, json) =>
      if (deserializer.isDefinedAt(json)) deserializer(json)
      else throw new Exception("Can't convert " + json + " to " + ttag.tpe)
  }
  val ttag: ru.TypeTag[R] = implicitly[ru.TypeTag[R]]
}
