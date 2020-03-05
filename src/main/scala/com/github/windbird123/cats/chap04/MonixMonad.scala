package com.github.windbird123.cats.chap04

object MonixMonad {
  import cats.Monad
  import monix.eval.Task

  def main(args: Array[String]): Unit = {
    Monad[Task].pure(3)
    Monad[Task].pure(4)
  }
}
