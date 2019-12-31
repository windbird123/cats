package com.github.windbird123.cats.chap4

object MonixMonad {
  import cats.Monad
  import monix.eval.Task

  def main(args: Array[String]): Unit = {
    val m1 = Monad[Task].pure(3)
    val m2 = Monad[Task].pure(4)
  }
}
