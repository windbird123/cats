package com.github.windbird123.cats.chap6

object ValidatedCreation extends App {
  import cats.syntax.validated._

  123.valid[List[String]]
  List("Badness").invalid[Int]

  import cats.data.Validated
  import cats.instances.list._
  import cats.syntax.applicative._
  import cats.syntax.applicativeError._

  type ErrorOr[A] = Validated[List[String], A]
  123.pure[ErrorOr]
  List("Badness").raiseError[ErrorOr, Int]
}

object ValidatedCombination extends App {
  import cats.data.Validated
  type AllErrorOr[A] = Validated[String, A]

  import cats.syntax.apply._ // for tupled
  import cats.syntax.validated._
  import cats.instances.vector._

  val out = (Vector(404).invalid[Int], Vector(500).invalid[Int], 3.valid[Vector[Int]]).tupled
//  val out = (2.valid[Vector[Int]], 3.valid[Vector[Int]]).tupled
  println(out)
}
