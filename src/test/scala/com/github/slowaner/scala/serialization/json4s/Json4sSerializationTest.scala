package com.github.slowaner.scala.serialization.json4s

import java.lang
import java.time.{LocalDateTime, OffsetDateTime, ZonedDateTime}

import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s.JsonDSL._
import org.json4s._
import org.scalatest._

private case class MockClass(
  mockString: String,
  mockInt: Int,
  mockDouble: Double,
  mockIntSet: Set[Int],
  mockDoubleSet: Set[Double],
  mockStringMap: Map[String, String]
)

class Json4sSerializationTest extends FlatSpec with Matchers {

  object Serialization extends JsonSerialization {
    override val customSerializers: Serializers[Any, JValue] = null
    override val customDeserializers: Deserializers[Any, JValue] = null
    override val customJson4sFormats: Formats = null
  }

  "A JsonSerialization" must """serialize (String) "sTr" to JString(sTr)""" in {
    Serialization.serialize("sTr") should be(JString("sTr"))
  }

  it must """serialize (int) 3 to JInt(3)""" in {
    Serialization.serialize(3) should be(JInt(3))
  }

  it must """serialize (java.lang.Integer) 3 to JInt(3)""" in {
    Serialization.serialize(new lang.Integer(3)) should be(JInt(3))
  }

  it must """serialize (double) 3.1d to JInt(3)""" in {
    Serialization.serialize(3.1d) should be(JDouble(3.1))
  }

  it must """serialize (java.lang.Double) 3.1d to JInt(3)""" in {
    Serialization.serialize(new lang.Double(3.1d)) should be(JDouble(3.1))
  }

  private val ldd = LocalDateTime.now()
  private val lddStr = ldd.toString
  it must s"""serialize (LocalDateTime) $lddStr to JString($lddStr)""" in {
    Serialization.serialize(ldd) should be(JString(lddStr))
  }

  private val odd = OffsetDateTime.now()
  private val oddStr = odd.toString
  it must s"""serialize (OffsetDateTime) $oddStr to JString($oddStr)""" in {
    Serialization.serialize(odd) should be(JString(oddStr))
  }

  private val zdd = ZonedDateTime.now()
  private val zddStr = zdd.toString
  it must s"""serialize (ZonedDateTime) $zddStr to JString($zddStr)""" in {
    Serialization.serialize(zdd) should be(JString(zddStr))
  }

  private val mci = MockClass(
    mockString = "mockStringValue",
    mockInt = 9857,
    mockDouble = 5.2d,
    mockIntSet = Set(6, 1, 2),
    mockDoubleSet = Set(9.1, 22.55543),
    mockStringMap = Map("mockStringMap-Key_1" -> "mockStringMap-Value_1", "mockStringMap-Key_2" -> "mockStringMap-Value_2")
  )
  private val mciJson = ("mockString" -> "mockStringValue") ~
    ("mockInt" -> 9857) ~
    ("mockDouble" -> 5.2) ~
    ("mockIntSet" -> Set(6, 1, 2)) ~
    ("mockDoubleSet" -> Set(9.1, 22.55543)) ~
    ("mockStringMap" ->
      ("mockStringMap-Key_1" -> "mockStringMap-Value_1") ~
        ("mockStringMap-Key_2" -> "mockStringMap-Value_2")
      )
  private val mciStr = mci.toString
  private val mciJsonStr = mciJson.toString

  it must s"""serialize $mciStr to $mciJsonStr""" in {
    Serialization.serialize(mci) should be(mciJson)
  }

  it must s"""deserialize $mciJsonStr to $mciStr""" in {
    Serialization.deserialize[MockClass](mciJson) should be(mci)
  }
}
