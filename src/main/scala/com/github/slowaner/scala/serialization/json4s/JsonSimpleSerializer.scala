package com.github.slowaner.scala.serialization.json4s

import org.json4s._

import com.github.slowaner.scala.serialization

trait JsonSimpleSerializer[-M] extends JsonSerializer[M] with serialization.SimpleSerializer[M, JValue]
