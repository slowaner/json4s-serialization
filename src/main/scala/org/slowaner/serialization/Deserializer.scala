package org.slowaner.serialization

import scala.reflect.runtime.{universe => ru}

trait Deserializer[-T, +R] {

  def deserialize: PartialFunction[(ru.TypeTag[_], T), R]

}

