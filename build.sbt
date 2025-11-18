ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "LetsTryStreamsMinimal"
  )

val PekkoVersion = "1.1.3"
val PekkoHttpVersion = "1.1.0"
libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-connectors-csv" % "1.1.0",
  "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
  "org.apache.pekko" %% "pekko-actor" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion
)
