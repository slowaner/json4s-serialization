package com.github.slowaner.scala.serialization.json4s

import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.serialization.FieldSerializer
import org.json4s.{JObject, JValue}

trait JsonFieldSerializer[T] extends JsonSerializer[T] with FieldSerializer[T, JValue] {

  val jsonSerialization: JsonSerialization

  override protected def serializeFields(obj: T, serFields: Iterable[ru.TermSymbol]): JValue = {
    initFieldsMirrorWith(obj)
    JObject(serFields.map(f => {
      val fBind = fieldsMirrorsByTermSymbol(f)
      fBind.fName -> jsonSerialization.serialize(fBind.fMirror.get)
    }).toList)
  }
}
