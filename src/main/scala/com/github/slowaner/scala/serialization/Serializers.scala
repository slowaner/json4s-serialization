package com.github.slowaner.scala.serialization

trait Serializers[-T <: Any, +R] {
  val customSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serialize)
  }
  val customSelfSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeSelf)
  }
  val customAllSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeAll)
  }

  def ++[K <: T, N >: R](newSerializers: Serializers[K, N]): Serializers[K, N] = copy(cstmSerializers = customSerializers ++ newSerializers.customSerializers)

  def :+[K <: T, N >: R](newSerializer: Serializer[K, N]): Serializers[K, N] = copy(cstmSerializers = customSerializers :+ newSerializer)

  def copy[K <: T, N >: R](cstmSerializers: Seq[Serializer[K, N]] = customSerializers): Serializers[K, N] = new Serializers[K, N]() {
    override def customSerializers: Seq[Serializer[K, N]] = cstmSerializers
  }

  def customWithSerializer(fieldsNames: Set[String]): PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeWith(fieldsNames))
  }

  protected def customSerializers: Seq[Serializer[T, R]]
}
