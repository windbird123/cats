package com.github.windbird123.cats.chap04

object CatsEitherTest {
  import cats.syntax.either._

  def main(args: Array[String]): Unit = {

    val a = 3.asRight[String]
    val b = 4.asRight[String]

    for {
      x <- a
      y <- b
    } yield x * x + y * y // Right(25)
  }
}
