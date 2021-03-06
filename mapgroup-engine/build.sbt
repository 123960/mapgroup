name := """mapgroup-engine"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.12.0"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.4"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.8"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.8"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.4"

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"
