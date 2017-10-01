package org.slowaner.serialization

trait Serializers[-T <: Any, +R] {
  protected def customSerializers: Seq[Serializer[T, R]]

  def ++[K <: T, N >: R](newSerializers: Serializers[K, N]): Serializers[K, N] = copy(cstmSerializers = customSerializers ++ newSerializers.customSerializers)

  def :+[K <: T, N >: R](newSerializer: Serializer[K, N]): Serializers[K, N] = copy(cstmSerializers = customSerializers :+ newSerializer)

  val customSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serialize)
  }

  val customSelfSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeSelf)
  }

  val customAllSerializer: PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeAll)
  }

  def customWithSerializer(fieldsNames: Set[String]): PartialFunction[T, R] = customSerializers.foldLeft(PartialFunction.empty[T, R]) { (acc, x) =>
    acc.orElse(x.serializeWith(fieldsNames))
  }

  def copy[K, N](cstmSerializers: Seq[Serializer[K, N]] = customSerializers): Serializers[K, N] = new Serializers[K, N]() {
    override def customSerializers: Seq[Serializer[K, N]] = cstmSerializers
  }
}
