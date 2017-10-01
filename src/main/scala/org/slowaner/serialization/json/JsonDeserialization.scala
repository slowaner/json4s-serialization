package org.slowaner.serialization.json

import org.json4s.JsonAST.JValue

import org.slowaner.serialization.Deserialization

trait JsonDeserialization extends Deserialization[JValue]
