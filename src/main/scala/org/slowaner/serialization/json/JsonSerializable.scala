package org.slowaner.serialization.json

import org.json4s.{JObject, JValue}

import org.slowaner.serialization.Serializable

trait JsonSerializable extends Serializable[JValue] with Implicits {

  override def serializeSelf: JValue = serializeWith(selfFieldsNames)

  override def serializeAll: JValue = serializeWith(selfFieldsNames ++ relationFieldsNames)

  override def serializeWith(fieldsNames: Traversable[String]): JValue =
    JObject(getSerializedValuesForNames(fieldsNames).toList)
}
