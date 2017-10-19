package com.github.slowaner.scala.serialization.json4s

import scala.language.postfixOps
import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.reflections4s.{CaseClassFactory, ReflectionHelper}
import com.github.slowaner.scala.serialization.CaseClassDeserializer
import org.json4s._

class JsonCaseClassDeserializer(deserialization: JsonDeserialization) extends CaseClassDeserializer[JValue] {

  override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), Any] = {
    case (ttag, json) if ttag.tpe.typeSymbol.isClass && ttag.tpe.typeSymbol.asClass.isCaseClass =>
      json match {
        case JObject(fields) =>
          val factory = factoryCache.getOrElse(ttag, {
            val fact = CaseClassFactory(ttag)
            factoryCache += ttag -> fact
            fact
          })
          factory.deserializeWith(
            fields map {
              case (key, vl) =>
                key -> deserialization.deserialize(vl)(ReflectionHelper.typeToTypeTag(factory.defaultConstructorParams.find(_.name.toString == key).get.typeSignature))
            } toMap)
        case _ => throw new IllegalArgumentException("Passed parameter must be an JObject")
      }
  }
  private[this] var factoryCache = Map[ru.TypeTag[_], CaseClassFactory[_]]()
}
