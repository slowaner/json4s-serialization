import sbt.Keys._
import sbt._
import xerial.sbt.pack.PackPlugin

val organizationName = "org.slowaner"
val rootProjectName = "json4s-serialization"

val reflections4sProjectUri = uri("git://github.com/slowaner/reflections4s.git#master")

// Versions
// Testing
val scalatestVersion = "3.0.3"
val junitVersion = "4.12"

// Json4s
val json4sVersion = "3.5.3"

// Persistence
val persistenceVersion = "2.2"

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
    "javax.persistence" % "javax.persistence-api" % persistenceVersion
  )
)

lazy val root = Project(rootProjectName, file("."))
  .settings(commonSettings ++ rootSettings)
  .enablePlugins(PackPlugin)
  .dependsOn(reflections4sProject)

lazy val reflections4sProject = RootProject(reflections4sProjectUri)
