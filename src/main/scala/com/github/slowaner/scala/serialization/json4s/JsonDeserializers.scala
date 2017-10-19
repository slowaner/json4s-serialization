package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization.Deserializers
import org.json4s.JValue

class JsonDeserializers[+R](deserializers: JsonDeserializer[R]*) extends Deserializers[JValue, R] {

  def this(serializers: Iterable[JsonDeserializer[R]]) = this(serializers.toSeq: _*)

  override protected def customDeserializers: Seq[JsonDeserializer[R]] = deserializers
}
