package org.slowaner.serialization

trait SimpleSerializer[M, R] extends Serializer[M, R] {

  override final def serializeAll: PartialFunction[M, R] = this.serializeSelf

  override final def serializeWith(fieldsNames: Set[String]): PartialFunction[M, R] = this.serializeSelf
}
