package com.github.slowaner.scala.serialization

import scala.reflect.runtime.{universe => ru}

trait Deserializers[-T, +R] {
  val customDeserializer: PartialFunction[(ru.TypeTag[_], T), R] = customDeserializers.foldLeft(PartialFunction.empty[(ru.TypeTag[_], T), R]) { (acc, x) =>
    acc.orElse(x.deserialize)
  }

  def ++[K <: T, N >: R](newDeserializers: Deserializers[K, N]): Deserializers[K, N] = copy(cstmDeserializers = customDeserializers ++ newDeserializers.customDeserializers)

  def :+[K <: T, N >: R](newDeserializer: Deserializer[K, N]): Deserializers[K, N] = copy(cstmDeserializers = customDeserializers :+ newDeserializer)

  def copy[K <: T, N >: R](cstmDeserializers: Seq[Deserializer[K, N]] = customDeserializers): Deserializers[K, N] = new Deserializers[K, N]() {
    override def customDeserializers: Seq[Deserializer[K, N]] = cstmDeserializers
  }

  protected def customDeserializers: Seq[Deserializer[T, R]]
}
