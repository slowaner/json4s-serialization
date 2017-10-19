package com.github.slowaner.scala.serialization

trait Serializer[-M, +R] {

  final def serialize: PartialFunction[M, R] = serializeSelf

  def serializeSelf: PartialFunction[M, R]

  def serializeAll: PartialFunction[M, R]

  def serializeWith(fieldsNames: Set[String]): PartialFunction[M, R]
}

