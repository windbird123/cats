package com.github.windbird123.cats.chap04

object CatsWriterTest {
  def main(args: Array[String]): Unit = {
    import cats.data.Writer
    import cats.instances.vector._ // for Monoid

    val w1 = for {
      a <- Writer(Vector.empty[String], 10)
      _ <- Writer(Vector("a", "b"), ())
      b <- Writer(Vector("x", "y", "z"), 32)
    } yield a + b

    // 로깅이 되는 Vector 들 간에는 Monoid 연산을 함
    val out = w1.run
    println(out) // (Vector(a, b, x, y, z),42)
  }
}

object FactorialShow {
  def slowly[A](body: => A): A =
    try body
    finally Thread.sleep(100)

  import cats.data.Writer
  import cats.instances.vector._ // for Monoid

  def factorial(n: Int): Writer[Vector[String], Int] = {
    val ans = slowly(if (n == 0) {
      Writer(Vector.empty[String], 1)
    } else {
      factorial(n - 1).map(_ * n)
    })

    for {
      x <- ans
      _ <- Writer(Vector(s"fact $n $x"), 0)
    } yield x
  }

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent._
    import scala.concurrent.duration._

    val futures = Vector(Future(factorial(3)), Future(factorial(3)))
    val out = Await.result(Future.sequence(futures), 5.seconds)

    out.foreach { x =>
      x.written.foreach(println)
    }
  }
}
