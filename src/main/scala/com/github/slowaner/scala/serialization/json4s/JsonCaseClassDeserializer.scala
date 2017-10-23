package com.github.slowaner.scala.serialization.json4s

import scala.language.postfixOps
import scala.reflect.runtime.{universe => ru}

import com.github.slowaner.scala.reflections4s.{CaseClassFactory, ReflectionHelper}
import com.github.slowaner.scala.serialization.CaseClassDeserializer
import org.json4s._

class JsonCaseClassDeserializer(deserialization: JsonDeserialization) extends CaseClassDeserializer[JValue] {

  private[this] var factoryCache = Map[ru.TypeTag[_], CaseClassFactory[_]]()

  override val deserialize: PartialFunction[(ru.TypeTag[_], JValue), Any] = {
    case (ttag, json) if ttag.tpe.typeSymbol.isClass && ttag.tpe.typeSymbol.asClass.isCaseClass =>
      json match {
        case JObject(fields) =>
          val factory = factoryCache.getOrElse(ttag, {
            val fact = CaseClassFactory(ttag)
            factoryCache += ttag -> fact
            fact
          })
          val nFields = fields.filter {
            case (key, value) => factory.defaultConstructorParams.exists(_.name.toString == key)
          }.map({
            case (key, vl) =>
              factory.defaultConstructorParams.find(_.name.toString == key) match {
                case Some(value) =>
                  val ttag = ReflectionHelper.typeToTypeTag(value.typeSignature)
                  key -> deserialization.deserialize(vl)(ttag)
                case _ => key -> vl
              }
          }).toMap[String, Any]
          factory.deserializeWith(nFields)
        case _ => throw new IllegalArgumentException("Passed parameter must be an JObject")
      }
  }
}
