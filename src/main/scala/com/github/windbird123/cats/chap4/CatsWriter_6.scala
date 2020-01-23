package com.github.windbird123.cats.chap4

object CatsWriterTest extends App {
  import cats.data.Writer

  Writer(Vector("It was the best of times", "it was the worst of times"), 1859)

  import cats.instances.vector._
  import cats.syntax.applicative._

  type Logged[A] = Writer[Vector[String], A]
  123.pure[Logged]

  import cats.syntax.writer._
  Vector("msg1", "msg2").tell // WriterT((Vector(msg1, msg2, msg3),())), no result
  val b = 123.writer(Vector("msg1", "msg2"))

  val a = Writer(Vector("msg1", "msg2"), 123)

  val aResult: Int = a.value
  val aLog: Vector[String] = a.written

  // log: scala.collection.immutable.Vector[String] = Vector(msg1, msg2, msg3)
  // result: Int = 123
  val (log, result) = b.run

  val writer1 = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b

  val writer1Out = writer1.run
  println(writer1Out) // (Vector(a, b, c, x, y, z),42)

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase)) // WriterT((Vector(A, B, C, X, Y, Z),42))

  val writer3 = writer1.bimap(log => log.map(_.toUpperCase()), res => res * 100) // WriterT((Vector(A, B, C, X, Y, Z),4200))
}

object ShowWorkingExercise extends App {
  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(100)

  import cats.data.Writer
  import cats.instances.list._

  def factorial(n: Int): Writer[List[String], Int] = {
    for {
      ans <- if (n == 0) Writer(List.empty[String], 1)
      else {
        slowly(factorial(n - 1).map(_ * n))
      }

      _ <- Writer(List(""), ())

    } yield ans
  }

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._
  import scala.concurrent.duration._

  val out = Await.result(
    Future.sequence(Vector(Future(factorial(3).run), Future(factorial(5).run))),
    5.seconds
  )

  println(out)
}
