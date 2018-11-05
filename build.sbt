val catsVersion = "1.0.1"
val catsCore = "org.typelevel" %% "cats-core" % catsVersion
val catsFree = "org.typelevel" %% "cats-free" % catsVersion
val catsMtl = "org.typelevel" %% "cats-mtl-core" % "0.2.1"

val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.11.0"
val macroParadise = compilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
val kindProjector = compilerPlugin(
  "org.spire-math" %% "kind-projector" % "0.9.4")
val resetAllAttrs = "org.scalamacros" %% "resetallattrs" % "1.0.0"

val specs2Version = "3.6" // use the version used by discipline
val specs2Core = "org.specs2" %% "specs2-core" % specs2Version
val specs2Scalacheck = "org.specs2" %% "specs2-scalacheck" % specs2Version
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.4"

val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.26"
val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % "7.2.26"

//scalacOptions += "-Ypartial-unification"

lazy val root = (project in file(".")).settings(
  organization := "com.example",
  name := "herding-cats",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    catsCore,
    catsFree,
    catsMtl,
//      simulacrum,
//      specs2Core % Test, specs2Scalacheck % Test, scalacheck % Test,
    scalazCore,
    scalazConcurrent,
    macroParadise,
    kindProjector,
    resetAllAttrs
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-Ypartial-unification",
    "-feature",
    "-language:_"
  )
)
