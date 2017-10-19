package com.github.slowaner.scala.serialization

import scala.reflect.runtime.{universe => ru}

trait Deserializer[-T, +R] {
  val deserialize: PartialFunction[(ru.TypeTag[_], T), R]
}

