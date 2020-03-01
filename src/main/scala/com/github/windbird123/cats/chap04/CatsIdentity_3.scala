package com.github.windbird123.cats.chap04

object IdentityMonadTest {
  import cats.Monad
  import cats.syntax.functor._ // for map
  import cats.syntax.flatMap._ // for flatMap

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  def main(args: Array[String]): Unit = {
    //sumSquare(3, 4)
    // 위와 같이 하면 에러가 나는데, 에러가 안나게 할려면?

    import cats.Id
    sumSquare[Id](3, 4)
  }
}

// Implement pure, map, and flatMap for Id
import cats.Id
trait IdMonad {
  def pure[A](a: A): Id[A] = a
  def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
  def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
}
