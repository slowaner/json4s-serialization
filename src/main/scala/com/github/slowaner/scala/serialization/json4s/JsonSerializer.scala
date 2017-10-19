package com.github.slowaner.scala.serialization.json4s

import com.github.slowaner.scala.serialization
import org.json4s._

trait JsonSerializer[-M] extends serialization.Serializer[M, JValue]
