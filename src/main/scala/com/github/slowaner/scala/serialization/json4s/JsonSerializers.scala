package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization.Serializers
import org.json4s.JValue

class JsonSerializers[-T](serializers: JsonSerializer[T]*) extends Serializers[T, JValue] {

  def this(serializers: Iterable[JsonSerializer[T]]) = this(serializers.toSeq: _*)

  override protected def customSerializers: Seq[JsonSerializer[T]] = serializers
}
