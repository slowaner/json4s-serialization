package org.slowaner.serialization.json

import org.json4s.{JObject, JValue}

import scala.reflect.runtime.universe

import org.slowaner.serialization.FieldSerializer

trait JsonFieldSerializer[T] extends JsonSerializer[T] with FieldSerializer[T, JValue] {
  override protected def serializeFields(obj: T, serFields: Iterable[universe.TermSymbol]): JValue = {
    initFieldsMirrorWith(obj)
    JObject(serFields.map(f => {
      val fBind = fieldsMirrorsByTermSymbol(f)
      fBind.fName -> JsonSerialization.serialize(fBind.fMirror.get)
    }).toList)
  }
}
