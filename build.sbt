name := """elm-tac-toe-backend"""
organization := "com.amazzeo"

version := "1.0-SNAPSHOT"

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Bintary JCenter" at "http://jcenter.bintray.com"

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "play-circe" %% "play-circe" % "2.6-0.8.0"

val circeVersion = "0.8.0"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.amazzeo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.amazzeo.binders._"

lazy val buildFrontEnd = taskKey[Unit]("Runs 'yarn build' to use webpack to build the Elm app.")

buildFrontEnd := {
  val buildOutput = Seq("yarn", "build") !!

  println(buildOutput)
}

