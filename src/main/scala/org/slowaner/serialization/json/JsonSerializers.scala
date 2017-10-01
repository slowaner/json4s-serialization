package org.slowaner.serialization.json

import org.json4s.JValue

import org.slowaner.serialization.Serializers

class JsonSerializers[-T](serializers: JsonSerializer[T]*) extends Serializers[T, JValue] {

  override protected def customSerializers: Seq[JsonSerializer[T]] = serializers

  def this(serializers: Iterable[JsonSerializer[T]]) = this(serializers.toSeq: _*)
}
