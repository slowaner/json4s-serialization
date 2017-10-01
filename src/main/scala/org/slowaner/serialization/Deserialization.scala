package org.slowaner.serialization

import scala.reflect.runtime.{universe => ru}

trait Deserialization[-T] {
  def deserialize[R](a: T)(implicit ttag: ru.TypeTag[R]): R
}
