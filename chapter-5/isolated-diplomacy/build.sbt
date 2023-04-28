// See README.md for license details.

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.mark"

val chiselVersion = "3.5.1"

lazy val root = (project in file("."))
  .settings(
    name := "IsoDiplomacy",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % "0.5.1" % "test",
      "org.chipsalliance" %% "cde" % "0.1.2",
      "org.chipsalliance" %% "diplomacy" % "0.0.0-335-c7c8a6-DIRTYa46afa4d"

    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:genBundleElements",
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
  )

