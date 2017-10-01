package org.slowaner.serialization.json

import org.json4s.JValue

import scala.reflect.runtime.{universe => ru}

class JsonCustomDeserializer[R: ru.TypeTag](deserializer: PartialFunction[JValue, R]) extends JsonDeserializer[R] {
  val ttag: ru.TypeTag[R] = implicitly[ru.TypeTag[R]]

  override def deserialize: PartialFunction[(ru.TypeTag[_], JValue), R] = {
    case (`ttag`, json) => if (deserializer.isDefinedAt(json)) deserializer(json)
    else throw new Exception("Can't convert " + json + " to " + ttag.tpe)
  }
}
