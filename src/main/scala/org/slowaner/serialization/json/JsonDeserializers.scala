package org.slowaner.serialization.json

import org.json4s.JValue

import scala.reflect.runtime.{universe => ru}

import org.slowaner.serialization.Deserializers

class JsonDeserializers[+R](deserializers: JsonDeserializer[R]*) extends Deserializers[JValue, R] {

  override protected def customDeserializers: Seq[JsonDeserializer[R]] = deserializers

  def this(serializers: Iterable[JsonDeserializer[R]]) = this(serializers.toSeq: _*)
}
