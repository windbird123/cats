package com.github.windbird123.cats.chap06

// tupled 와 mapN 을 알면 끝
object SemigroupalTest {
  def main(args: Array[String]): Unit = {
    import cats.instances.option._
    import cats.syntax.apply._
    import cats.syntax.option._

    (123.some, "abc".some, true.some).tupled // Some((123,abc,true))
    (123.some, "abc".some, none[String]).tupled // None

    val list1 = List("a", "b")
    val list2 = List(1, 2, 3)

    import cats.instances.list._
    (list1, list2).tupled // List((a,1), (a,2), (a,3), (b,1), (b,2), (b,3))

    import cats.instances.either._
    import cats.syntax.either._
    (1.asRight[String], 2.asRight[String]).tupled // Right((1,2))
    (1.asRight[String], 2.asRight[String], "a".asLeft[Int]).tupled // Left("a")

    val add = (a: Int, b: Int) => a + b
    (1.some, 2.some).mapN(add) // Some(3)
  }
}

import cats.Monad
object ProductDef {
  // product 를 flatMap 을 사용해 정의

  import cats.syntax.flatMap._
  import cats.syntax.functor._ // for map
  def product[M[_]: Monad, A, B](fa: M[A], fb: M[B]): M[(A, B)] =
    for {
      a <- fa
      b <- fb
    } yield (a, b)
}
