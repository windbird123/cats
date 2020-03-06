package com.github.windbird123.cats.chap05

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TransformerExercise {
  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map("Jazz" -> 6, "Bumblebee" -> 8, "Hot Rod" -> 10)

  import cats.instances.future._ // for flatMap
  def getPowerLevel(ally: String): Response[Int] = powerLevels.get(ally) match {
    case Some(avg) => EitherT.right(Future(avg))
    case None      => EitherT.left(Future(s"$ally unreachable"))
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = {
    val powerSum = for {
      x <- getPowerLevel(ally1)
      y <- getPowerLevel(ally2)
    } yield x + y

    powerSum.map(_ > 15)
  }

  def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value

    Await.result(stack, 1.second) match {
      case Left(msg) =>
        s"Comms error: $msg"
      case Right(true) =>
        s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) =>
        s"$ally1 and $ally2 need a recharge."
    }
  }
}
