package com.github.slowaner.scala.serialization

trait Serializable[+R] extends scala.Serializable {
  val selfFieldsNames: Set[String]
  val relationFieldsNames: Set[String]

  final def serialize: R = serializeSelf

  def serializeSelf: R

  def serializeAll: R

  def serializeWith(fieldsNames: Traversable[String]): R

  final def getSerializedValuesForNames(fieldsNames: Traversable[String]): Map[String, R] = fieldsNames.map(fn => fn -> getSerializedValueForName(fn)).toMap

  def getSerializedValueForName: (String) => R
}
