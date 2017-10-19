package com.github.slowaner.scala.serialization

import scala.reflect.runtime.{universe => ru}

trait Deserializable[-T] extends scala.Serializable {
  val selfFieldsNames: Set[String]
  val relationFieldsNames: Set[String]

  def deserialize(inp: T): this.type
}
