package com.github.windbird123.cats.chap5

import cats.data.{EitherT, OptionT}

import scala.concurrent.Await

object MonadTransformerTest extends App {
  type ErrorOr[A] = Either[String, A]
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  import cats.instances.either._
  import cats.syntax.either._

  type MyError[A] = Either[String, A]

  val a: MyError[Option[Int]] = Some(10).asRight[String]
  val b: MyError[Option[Int]] = Some(32).asRight[String]

  val c = for {
    x <- OptionT(a)
    y <- OptionT(b)
  } yield x + y
}

object MonadTransformExercise extends App {
  import cats.instances.future._
  import cats.syntax.either._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import scala.concurrent.duration._

  type Response[A] = Future[Either[String, A]]

  val powerLevels = Map("Jazz" -> 6, "Bumblebee" -> 8, "Hot Rod" -> 10)

  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case Some(k) => Future { k.asRight[String] }
      case None    => Future { s"not exist $autobot".asLeft[Int] }
    }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = {
    val outT = for {
      x <- EitherT(getPowerLevel(ally1))
      y <- EitherT(getPowerLevel(ally2))
    } yield (x + y > 15)

    outT.value
  }

  def tacticalReport(ally1: String, ally2: String): String = {
    val future = canSpecialMove(ally1, ally2)
    Await.result(future, 5.second) match {
      case Right(true)  => s"can"
      case Right(false) => s"cant"
      case Left(v)      => v
    }
  }

  val t1 = tacticalReport("Jazz", "Bumblebee")
  println(t1)

  val t2 = tacticalReport("Bumblebee", "Hot Rod")
  println(t2)

  val t3 = tacticalReport("Jazz", "Ironhide")
  println(t3)
}
