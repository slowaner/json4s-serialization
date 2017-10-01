package org.slowaner.serialization.json

import org.json4s._

import scala.reflect.runtime.{universe => ru}

import org.slowaner.serialization.Serializer

trait JsonSerializer[-M] extends Serializer[M, JValue]
