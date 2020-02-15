name := "cats"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.3"

scalacOptions := Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint", // enable handy linter warnings
  "-Xfatal-warnings", // turn compiler warnings into errors
  "-Ypartial-unification", // allow the compiler to unify type constructors of different arities
  "-language:implicitConversions"
)

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-core" % "1.0.0",
  "org.typelevel" %% "cats-core" % "1.4.0",
  "org.typelevel" %% "cats-effect" % "1.0.0",
  "io.monix" %% "monix" % "3.1.0",
  "org.typelevel" %% "simulacrum" % "1.0.0"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
