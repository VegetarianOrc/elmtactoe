name := """elm-tac-toe-backend"""
organization := "com.amazzeo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.amazzeo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.amazzeo.binders._"

lazy val buildFrontEnd = taskKey[Unit]("Runs 'yarn build' to use webpack to build the Elm app.")

buildFrontEnd := {
  val buildOutput = Seq("yarn", "build") !!

  println(buildOutput)
}

