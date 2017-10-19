package com.github.slowaner.scala.serialization

import javax.persistence.Transient

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

trait FieldSerializer[T, +R] extends Serializer[T, R] {

  lazy val mirror: ru.Mirror = ttag.mirror
  lazy val tpe: ru.Type = ttag.tpe
  lazy val fields: Iterable[ru.TermSymbol] = tpe.decls.filter(d => d.isTerm && (d.asTerm.isVal || d.asTerm.isVar) && (!d.annotations.exists(a => a.tree.tpe <:< ru.typeOf[Transient]))).map(_.asTerm)
  lazy val fieldsByNames: Map[String, ru.TermSymbol] = fields.map(f => f.name.toString.trim -> f).toMap
  val ttag: ru.TypeTag[T]
  val ctag: ClassTag[T]
  val selfFields: Iterable[ru.TermSymbol]
  val relationsFields: Iterable[ru.TermSymbol]
  var fieldsMirrorsByTermSymbol: Map[ru.TermSymbol, BondField] = _
  var fieldsMirrorsByName: Map[String, BondField] = _

  override def serializeSelf: PartialFunction[Any, R] = {
    case obj: Serializable[R] => obj.serializeSelf
    case obj if ctag.runtimeClass.isInstance(obj) => serializeFields(obj.asInstanceOf[T], selfFields)
  }

  override def serializeAll: PartialFunction[Any, R] = {
    case obj: Serializable[R] => obj.serializeAll
    case obj if ctag.runtimeClass.isInstance(obj) => serializeFields(obj.asInstanceOf[T], fields)
  }

  override def serializeWith(fieldsNames: Set[String]): PartialFunction[Any, R] = {
    case obj: Serializable[R] => obj.serializeWith(fieldsNames)
    case obj if ctag.runtimeClass.isInstance(obj) => serializeFields(obj.asInstanceOf[T], fieldsByNames.filterKeys(fName => fieldsNames.contains(fName)).values)
  }

  protected final def initFieldsMirrorWith(obj: T): Unit = {
    if (obj != null) {
      fieldsMirrorsByTermSymbol = if (fieldsMirrorsByTermSymbol != null) fieldsMirrorsByTermSymbol.mapValues(bndF => bndF.copy(fMirror = bndF.fMirror.bind(obj)))
      else {
        val m = mirror.reflect(obj)(ctag)
        fieldsByNames.map({ case (fName, f) => f -> BondField(fName, m.reflectField(f)) })
      }
      fieldsMirrorsByName = fieldsMirrorsByTermSymbol.map({ case (_, fBind) => fBind.fName -> fBind })
    }
    else throw new NullPointerException
  }

  protected def serializeFields(obj: T, serFields: Iterable[ru.TermSymbol]): R

  case class BondField(fName: String, fMirror: ru.FieldMirror)
}
