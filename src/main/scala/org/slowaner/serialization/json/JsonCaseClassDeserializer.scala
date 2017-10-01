package org.slowaner.serialization.json

import org.json4s._

import scala.reflect.runtime.{universe => ru}

import org.slowaner.reflections4s.CaseClassFactory
import org.slowaner.serialization.CaseClassDeserializer

object JsonCaseClassDeserializer extends CaseClassDeserializer[JValue] {

  private[this] var factoryCache = Map[ru.TypeTag[_], CaseClassFactory[_]]()

  //  override protected def deserialize(inp: JValue): Any = new Exception("Use other method for deserialize case classes")

  override def deserialize: PartialFunction[(ru.TypeTag[_], JValue), Any] = {
    case (ttag, json) if ttag.tpe.typeSymbol.isClass && ttag.tpe.typeSymbol.asClass.isCaseClass =>
      json match {
        case JObject(fields) =>
          val factory = factoryCache.getOrElse(ttag, {
            val fact = CaseClassFactory(ttag)
            factoryCache += ttag -> fact
            fact
          })
          factory.deserializeWith(fields.toMap.mapValues(_.values))
        case _ => throw new IllegalArgumentException("Passed parameter must be an JObject")
      }
  }
}
