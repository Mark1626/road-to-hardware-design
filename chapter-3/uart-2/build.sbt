// See README.md for license details.

ThisBuild / scalaVersion     := "2.12.13"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.mark"

val chiselVersion = "3.5.6"

lazy val root = (project in file("."))
  .settings(
    name := "Uart",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % "0.5.6",
      "me.sequencer"     % "cde_2.13"      % "v0.1.0-17-fcde29",
      "edu.berkeley.cs" % "ip-contributions" % "0.5.1"
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

