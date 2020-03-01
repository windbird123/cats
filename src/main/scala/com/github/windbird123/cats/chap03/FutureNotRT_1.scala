package com.github.windbird123.cats.chap03

import scala.concurrent.Future
import scala.util.Random

object FutureNotRT {
  // Future is NOT referentially transparent
  def main(args: Array[String]): Unit = {
    val r = new Random(0L)

    import scala.concurrent.ExecutionContext.Implicits.global
    val x = Future(r.nextInt)

    for {
      a <- x
      b <- x
    } yield (a, b)
  }
}
