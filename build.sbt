import sbt.Keys._
import sbt._
import xerial.sbt.pack.PackPlugin

val organizationName = "com.github.slowaner.scala"
val rootProjectName = "json4s-serialization"

// Versions
// Testing
val scalatestVersion = "3.0.3"
val junitVersion = "4.12"

// Json4s
val json4sVersion = "3.5.3"

// Persistence
val persistenceVersion = "2.2"

// Reflections4s
val reflections4sVersion = "0.0.5-SNAPSHOT"

resolvers += Opts.resolver.sonatypeSnapshots

lazy val commonSettings = Defaults.defaultConfigs ++ Seq(
  organization := organizationName,
  scalaVersion := "2.12.3",
  crossPaths := false,
  libraryDependencies ++= Seq(
    // Testing
    "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    "junit" % "junit" % junitVersion % Test,
  ),
  excludeFilter := new SimpleFileFilter(f => f.getName match {
    case ".gitignore" | ".gitkeep" => true
    case _ => false
  })
)

lazy val rootSettings = PackPlugin.packSettings ++ Seq(
  name := rootProjectName,
  libraryDependencies ++= Seq(
    // Json4s
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "org.json4s" %% "json4s-ext" % json4sVersion,
    // Persistence
    "javax.persistence" % "javax.persistence-api" % persistenceVersion,
    // Reflections4s
    "com.github.slowaner.scala" % "reflections4s" % reflections4sVersion
  )
)

lazy val root = Project(rootProjectName, file("."))
  .settings(commonSettings ++ rootSettings)
  .enablePlugins(PackPlugin)
