name := "stock-analysis"

version := "1.0"

scalaVersion := "2.10.3"

mainClass in Compile := Some("StockAnalysis")

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.7.7",
  "com.h2database" % "h2" % "1.4.182",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "joda-time" % "joda-time" % "2.5",
  "org.joda" % "joda-convert" % "1.7"
)
