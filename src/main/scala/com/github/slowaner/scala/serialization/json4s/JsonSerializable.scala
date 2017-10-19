package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization.Serializable
import org.json4s.{JObject, JValue}

trait JsonSerializable extends Serializable[JValue] with Implicits {

  override def serializeSelf: JValue = serializeWith(selfFieldsNames)

  override def serializeAll: JValue = serializeWith(selfFieldsNames ++ relationFieldsNames)

  override def serializeWith(fieldsNames: Traversable[String]): JValue =
    JObject(getSerializedValuesForNames(fieldsNames).toList)
}
