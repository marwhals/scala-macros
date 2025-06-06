ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.5.2"

ThisBuild / scalacOptions ++= Seq(
  "-Xprint:postInlining", // Can see Scala code after the "inline" step
  "-Xmax-inlines:100000"
)

lazy val root = (project in file("."))
  .settings(
    name := "scala-macros",
    libraryDependencies += "org.postgresql" % "postgresql" % "42.7.6"
  )
