package com.github.windbird123.cats.chap05

import cats.Monad

object MonadComposeTrial {
  // List, Option Monad 를 합성한다고 생각해 보자

  import cats.instances.list._
  import cats.instances.option._
  import cats.syntax.applicative._

  type Composed[A] = List[Option[A]]
  def composeTrial(): Monad[Composed] =
    new Monad[Composed] {
      // pure 는 쉽게 해결되는데..
      override def pure[A](x: A): Composed[A] = x.pure[Option].pure[List]

      // Option 이라는 정보만 있으면 아래와 같이 할 수 있을텐데..
      override def flatMap[A, B](
        fa: Composed[A]
      )(f: A => Composed[B]): Composed[B] = fa.flatMap {
        case Some(x) => f(x)
        case None    => None.pure[List]
      }

      override def tailRecM[A, B](a: A)(
        f: A => Composed[Either[A, B]]
      ): Composed[B] = ???
    }
}

object MonadStackTest {
  import cats.data.Writer
  import cats.instances.list._

  type Logged[A] = Writer[List[String], A]

  def parseNumber(str: String): Logged[Option[Int]] =
    util.Try(str.toInt).toOption match {
      case Some(num) => Writer(List(s"Read $str"), Some(num))
      case None      => Writer(List(s"Failed on $str"), None)
    }

  def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {
    import cats.data.OptionT

    val result = for {
      a <- OptionT(parseNumber(a))
      b <- OptionT(parseNumber(b))
      c <- OptionT(parseNumber(c))
    } yield a + b + c

    result.value
  }

  def main(args: Array[String]): Unit = {
    addAll("1", "2", "3") // WriterT((List(Read 1, Read 2, Read 3),Some(6)))
    addAll("1", "a", "3") // WriterT((List(Read 1, Failed on a),None))
  }
}
