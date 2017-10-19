package com.github.slowaner.scala.serialization

trait Serialization[+T] {
  final def serialize(a: Any): T = serializeSelf(a)

  def serializeSelf(a: Any): T

  def serializeAll(a: Any): T

  def serializeWith(a: Any, fieldsNames: Set[String]): T
}
