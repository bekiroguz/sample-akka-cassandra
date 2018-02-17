name := "sample-akka-cassandra"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.82" exclude("com.datastax.cassandra", "cassandra-driver-core"),
  "com.datastax.dse" % "dse-java-driver-core" % "1.5.1",
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.82" % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)
