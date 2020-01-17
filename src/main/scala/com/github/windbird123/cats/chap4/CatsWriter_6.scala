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

//  writer1.run
  // res4: cats.Id[(Vector[String], Int)] = (Vector(a, b, c, x, y, z),42)
}
