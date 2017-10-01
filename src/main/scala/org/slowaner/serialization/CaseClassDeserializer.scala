package org.slowaner.serialization

import scala.reflect.runtime.{universe => ru}

trait CaseClassDeserializer[-T] extends Deserializer[T, Any] {
}
