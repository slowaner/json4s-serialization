package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization.Deserializer
import org.json4s._

trait JsonDeserializer[+R] extends Deserializer[JValue, R]
