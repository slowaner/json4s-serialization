package com.github.slowaner.scala.serialization.json4s

import java.lang
import java.time.format.DateTimeParseException
import java.time.{LocalDateTime, OffsetDateTime, ZonedDateTime}

import com.github.slowaner.scala.serialization.{Deserializers, Serializers}
import org.json4s
import org.json4s.JsonDSL._
import org.json4s._
import org.scalatest._

private case class MockSubClass(name: String, value: Int)

private case class MockClass(
  mockInt: Int,
  mockDouble: Double,
  mockString: String,
  mockIntSet: Set[Int],
  mockDoubleSet: Set[Double],
  mockStringSet: Set[String],
  mockIntMap: Map[String, Int],
  mockDoubleMap: Map[String, Double],
  mockStringMap: Map[String, String],
  mockSubMock: MockSubClass
)

class Json4sSerializationTest extends FlatSpec with Matchers {

  object Serialization extends JsonSerialization {
    override val customSerializers: Serializers[Any, JValue] = null
    override val customDeserializers: Deserializers[Any, JValue] = null
    override val customJson4sFormats: Formats = null
  }

  def prefixer(prefix: String): Any => String = str => prefix + String.valueOf(str)

  //--------------------------------------------------------------------------------
  // Defining variables
  val nullVal: Null = null

  // Primitives
  val intVal = 3
  val doubleVal = 1.1d
  val strVal = "sTr"

  // Collections
  val intList = List(intVal, intVal * 2, intVal * 3)
  val intSet = intList.toSet
  val intSeq = intList
  val doubleList = List(doubleVal, doubleVal * 2, doubleVal * 4)
  val doubleSet = doubleList.toSet
  val doubleSeq = doubleList
  val stringList = List(strVal, strVal * 2, strVal * 3)
  val stringSet = stringList.toSet
  val stringSeq = stringList

  val intMap = intList.indices.zip(intList).map(tupl => (prefixer("int_")(tupl._1), tupl._2)).toMap
  val doubleMap = doubleList.indices.zip(doubleList).map(tupl => (prefixer("double_")(tupl._1), tupl._2)).toMap
  val stringMap = stringList.indices.zip(stringList).map(tupl => (prefixer("String_")(tupl._1), tupl._2)).toMap

  // Dates
  private val localDateTimeVal = LocalDateTime.now()
  private val offsetDateTimeVal = OffsetDateTime.now()
  private val zonedDateTimeVal = ZonedDateTime.now()

  // Case classes
  private val msc = MockSubClass(
    name = "MockSubClass_name",
    value = 321
  )
  private val mci = MockClass(
    mockInt = intVal,
    mockDouble = doubleVal,
    mockString = strVal,
    mockIntSet = intSet,
    mockDoubleSet = doubleSet,
    mockStringSet = stringSet,
    mockIntMap = intMap,
    mockDoubleMap = doubleMap,
    mockStringMap = stringMap,
    mockSubMock = msc
  )
  private val mciJson = ("mockInt" -> intVal) ~
    ("mockDouble" -> doubleVal) ~
    ("mockString" -> strVal) ~
    ("mockIntSet" -> intSet) ~
    ("mockDoubleSet" -> doubleSet) ~
    ("mockStringSet" -> stringSet) ~
    ("mockIntMap" -> intMap) ~
    ("mockDoubleMap" -> doubleMap) ~
    ("mockStringMap" -> stringMap) ~
    ("mockSubMock" ->
      ("name" -> "MockSubClass_name") ~
        ("value" -> 321)
      )

  val mockJsonIntArray = JArray(List(1, 2, 3))
  val mockJsonDoubleArray = JArray(List(1.1d, 2.2d, 3.3d))
  val mockJsonStringArray = JArray(List("1_Str", "2_Str", "3_Str"))
  val mockJsonMixedArray = JArray(List(1, 2.2d, "3_Str"))
  val mockObject: JObject = ("int" -> 1) ~
    ("double" -> 2.2d) ~
    ("String" -> "3_Str") ~
    ("intArray" -> mockJsonIntArray) ~
    ("doubleArray" -> mockJsonDoubleArray) ~
    ("stringArray" -> mockJsonStringArray) ~
    ("mixedArray" -> mockJsonMixedArray)

  //--------------------------------------------------------------------------------
  // String serialization
  val strJson = JString(strVal)
  // Not Null String to Json
  "A JsonSerialization" must s"""serialize (String) "$strVal" to $strJson""" in {
    Serialization.serialize(strVal) should be(strJson)
  }
  // Json to Not Null String
  it must s"""deserialize $strJson to (String) "$strVal"""" in {
    Serialization.deserialize[String](strJson) should be(strVal)
  }
  // Null String to JNull
  it must s"""serialize (String) $nullVal to $JNull""" in {
    Serialization.serialize(nullVal: String) should be(JNull)
  }
  // JNull to String
  it must s"""deserialize $JNull to (String) $nullVal""" in {
    Serialization.deserialize[String](JNull) should be(nullVal: String)
  }
  // JNothing to String
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to String""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[String](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // Int serialization
  val intJson = JInt(intVal)
  // Int to Json
  it must s"""serialize (int) $intVal to $intJson""" in {
    Serialization.serialize(intVal) should be(intJson)
  }
  // Json to Int
  it must s"""deserialize $intJson to (int) $intVal""" in {
    Serialization.deserialize[Int](intJson) should be(intVal)
  }
  // JNull to Int
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNull to (int)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[Int](JNull)
    }
  }
  // JNothing to Int
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (int)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[Int](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // Java Integer serialization
  val integerVal = new java.lang.Integer(intVal)
  val integerJson = JInt(integerVal.intValue())
  // Java Integer to Json
  it must s"""serialize (java.lang.Integer) $integerVal to $integerJson""" in {
    Serialization.serialize(integerVal) should be(integerJson)
  }
  // Json to Java Integer
  it must s"""deserialize $integerJson to (java.lang.Integer) $integerVal""" in {
    Serialization.deserialize[java.lang.Integer](integerJson) should be(integerVal)
  }
  // JNull to Java Integer
  it must s"""deserialize $JNull to (java.lang.Integer) $nullVal""" in {
    Serialization.deserialize[java.lang.Integer](JNull) should be(nullVal: java.lang.Integer)
  }
  // JNothing to Java Integer
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (java.lang.Integer)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[java.lang.Integer](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // Double serialization
  val doubleJson = JDouble(doubleVal)
  val doubleDecimalJson = JDecimal(doubleVal)
  // Double to Json
  it must s"""serialize (double) $doubleVal to $doubleJson""" in {
    Serialization.serialize(doubleVal) should be(doubleJson)
  }
  // JDouble to Double
  it must s"""deserialize $doubleJson to (double) $doubleVal""" in {
    Serialization.deserialize[Double](doubleJson) should be(doubleVal)
  }
  // JDecimal to Double
  it must s"""deserialize $doubleDecimalJson to (double) $doubleVal""" in {
    Serialization.deserialize[Double](doubleDecimalJson) should be(doubleVal)
  }
  // JNull to Double
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNull to (double)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[Double](JNull)
    }
  }
  // JNothing to Double
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (double)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[Double](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // Java Double serialization
  val javaDoubleVal = new lang.Double(doubleVal)
  val javaDoubleJson = JDouble(javaDoubleVal)
  val javaDoubleDecimalJson = JDecimal(javaDoubleVal.doubleValue())
  // Java Double to Json
  it must s"""serialize (java.lang.Double) $javaDoubleVal to $javaDoubleJson""" in {
    Serialization.serialize(javaDoubleVal) should be(javaDoubleJson)
  }
  // Json to Java Double
  it must s"""deserialize $javaDoubleJson to (java.lang.Double) $javaDoubleVal""" in {
    Serialization.deserialize[java.lang.Double](javaDoubleJson) should be(javaDoubleVal)
  }
  // JNull to Java Double
  it must s"""deserialize $JNull to (java.lang.Double) $nullVal""" in {
    Serialization.deserialize[java.lang.Double](JNull) should be(nullVal: java.lang.Double)
  }
  // JNothing to Java Double
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (java.lang.Double)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[java.lang.Double](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // LocalDateTime serialization
  val localDateTimeJson = JString(localDateTimeVal.toString)
  val localDateTimeInvalidJson_1 = JString("invalid")
  val localDateTimeInvalidJson_2 = JString("")
  val localDateTimeInvalidJson_3 = JString(localDateTimeVal.toString + "invalid")
  // LocalDateTime to Json
  it must s"""serialize (LocalDateTime) $localDateTimeVal to $localDateTimeJson""" in {
    Serialization.serialize(localDateTimeVal) should be(localDateTimeJson)
  }
  // Json to LocalDateTime
  it must s"""deserialize $localDateTimeJson to (LocalDateTime) $localDateTimeVal""" in {
    Serialization.deserialize[LocalDateTime](localDateTimeJson) should be(localDateTimeVal)
  }
  // JNull to LocalDateTime
  it must s"""deserialize $JNull to (LocalDateTime) $nullVal""" in {
    Serialization.deserialize[LocalDateTime](JNull) should be(nullVal: LocalDateTime)
  }
  // Invalid Json 1 to LocalDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $localDateTimeInvalidJson_1 to (LocalDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[LocalDateTime](localDateTimeInvalidJson_1)
    }
  }
  // Invalid Json 2 to LocalDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $localDateTimeInvalidJson_2 to (LocalDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[LocalDateTime](localDateTimeInvalidJson_2)
    }
  }
  // Invalid Json 3 to LocalDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $localDateTimeInvalidJson_3 to (LocalDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[LocalDateTime](localDateTimeInvalidJson_3)
    }
  }
  // JNothing to LocalDateTime
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (LocalDateTime)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[LocalDateTime](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // OffsetDateTime serialization
  val offsetDateTimeJson = JString(offsetDateTimeVal.toString)
  val offsetDateTimeInvalidJson_1 = JString("invalid")
  val offsetDateTimeInvalidJson_2 = JString("")
  val offsetDateTimeInvalidJson_3 = JString(offsetDateTimeVal.toString + "invalid")
  // OffsetDateTime to Json
  it must s"""serialize (OffsetDateTime) $offsetDateTimeVal to $offsetDateTimeJson""" in {
    Serialization.serialize(offsetDateTimeVal) should be(offsetDateTimeJson)
  }
  // Json to OffsetDateTime
  it must s"""deserialize $offsetDateTimeJson to (OffsetDateTime) $offsetDateTimeVal""" in {
    Serialization.deserialize[OffsetDateTime](offsetDateTimeJson) should be(offsetDateTimeVal)
  }
  // JNull to OffsetDateTime
  it must s"""deserialize $JNull to (OffsetDateTime) $nullVal""" in {
    Serialization.deserialize[OffsetDateTime](JNull) should be(nullVal: OffsetDateTime)
  }
  // Invalid Json 1 to OffsetDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $offsetDateTimeInvalidJson_1 to (OffsetDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[OffsetDateTime](offsetDateTimeInvalidJson_1)
    }
  }
  // Invalid Json 2 to OffsetDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $offsetDateTimeInvalidJson_2 to (OffsetDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[OffsetDateTime](offsetDateTimeInvalidJson_2)
    }
  }
  // Invalid Json 3 to OffsetDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $offsetDateTimeInvalidJson_3 to (OffsetDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[OffsetDateTime](offsetDateTimeInvalidJson_3)
    }
  }
  // JNothing to OffsetDateTime
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (OffsetDateTime)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[OffsetDateTime](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // ZonedDateTime serialization
  val zonedDateTimeJson = JString(zonedDateTimeVal.toString)
  val zonedDateTimeInvalidJson_1 = JString("invalid")
  val zonedDateTimeInvalidJson_2 = JString("")
  val zonedDateTimeInvalidJson_3 = JString(zonedDateTimeVal.toString + "invalid")
  // ZonedDateTime to Json
  it must s"""serialize (ZonedDateTime) $zonedDateTimeVal to $zonedDateTimeJson""" in {
    Serialization.serialize(zonedDateTimeVal) should be(zonedDateTimeJson)
  }
  // Json to ZonedDateTime
  it must s"""deserialize $zonedDateTimeJson to (ZonedDateTime) $zonedDateTimeVal""" in {
    Serialization.deserialize[ZonedDateTime](zonedDateTimeJson) should be(zonedDateTimeVal)
  }
  // JNull to ZonedDateTime
  it must s"""deserialize $JNull to (ZonedDateTime) $nullVal""" in {
    Serialization.deserialize[ZonedDateTime](JNull) should be(nullVal: ZonedDateTime)
  }
  // Invalid Json 1 to ZonedDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $zonedDateTimeInvalidJson_1 to (ZonedDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[ZonedDateTime](zonedDateTimeInvalidJson_1)
    }
  }
  // Invalid Json 2 to ZonedDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $zonedDateTimeInvalidJson_2 to (ZonedDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[ZonedDateTime](zonedDateTimeInvalidJson_2)
    }
  }
  // Invalid Json 3 to ZonedDateTime
  it should s"""throw java.time.format.DateTimeParseException while deserialize $zonedDateTimeInvalidJson_3 to (ZonedDateTime)""" in {
    assertThrows[DateTimeParseException] {
      Serialization.deserialize[ZonedDateTime](zonedDateTimeInvalidJson_3)
    }
  }
  // JNothing to ZonedDateTime
  it should s"""throw org.json4s.package$$MappingException while deserialize $JNothing to (ZonedDateTime)""" in {
    assertThrows[MappingException] {
      Serialization.deserialize[ZonedDateTime](JNothing)
    }
  }

  //--------------------------------------------------------------------------------
  // Case class serialization
  it must s"""serialize $mci to $mciJson""" in {
    Serialization.serialize(mci) should be(mciJson)
  }

  it must s"""deserialize $mciJson to $mci""" in {
    Serialization.deserialize[MockClass](mciJson) should be(mci)
  }
}
