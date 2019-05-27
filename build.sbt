//import sbt.Keys._
import sbt._


name := "AdNetworks"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "io.spray" % "spray-json_2.11" % "1.3.2",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "1.0",
  "com.typesafe.akka" %%"akka-http-testkit-experimental" % "1.0",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.scalacheck" % "scalacheck_2.11" % "1.12.5",
  "net.databinder.dispatch" % "dispatch-core_2.11" % "0.11.0",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9"
)

mainClass in (Compile, packageBin) := Some("com.inneractive.networks.Inneractive360Network")