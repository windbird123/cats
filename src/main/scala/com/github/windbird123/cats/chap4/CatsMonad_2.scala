package com.github.windbird123.cats.chap4

import scala.concurrent.{Await, Future}

object CatsMonadTest {
  import cats.Monad
  import cats.instances.future._
  import cats.instances.option._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  def main(args: Array[String]): Unit = {
    val opt1 = Monad[Option].pure(3)
    Monad[Option].flatMap(opt1)(x => Some(x + 3))

    val fm = Monad[Future]
    val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
    val out = Await.result(future, 1.second)
    println(out)
  }
}

object MonadSyntaxTest {
  import cats.Monad
  import cats.instances.option._
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  def main(args: Array[String]): Unit = {
    val out = sumSquare(Option(3), Option(4))
    println(out)

    // sumSquare(3, 4) 도 working 하게 하고 싶다면???
    import cats.Id
    sumSquare(3: Id[Int], 4 : Id[Int])
    sumSquare[Id](3, 4)
  }
}


trait MyId {
  import cats.Id

  def pure[A](a: A) : Id[A] = a

  def map[A, B](initial: Id[A])(f: A => B) : Id[B] = f(initial)

  def flatMap[A, B](initial: Id[A])(f: A => Id[B]) : Id[B] = f(initial)
}

