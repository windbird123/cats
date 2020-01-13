package com.github.windbird123.cats.chap4

import cats.Monad

trait MonadErrorDef[F[_], E] extends Monad[F] {
  // Lift an error into the `F` context:
  def raiseError[A](e: E): F[A]

  // Handle an error, potentially recovering from it:
  def handleError[A](fa: F[A])(f: E => A): F[A]

  // Test an instance of `F`
  // failing if the predicate is not satisfied:
  def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
}

object MonadErrorTest extends App {
  import cats.MonadError
  import cats.instances.either._

  type ErrorOr[A] = Either[String, A]

  val monadError = MonadError[ErrorOr, String]


  val success = monadError.pure(42) // success: ErrorOr[Int] = Right(42)
  val failure = monadError.raiseError("Bad") // failure: ErrorOr[Nothing] = Left(Bad)

  import scala.util.Try
  import cats.instances.try_._
//  import cats.instances.either._
//  import cats.instances.int._

  val e : Throwable= new RuntimeException("It's all gone")

  import cats.syntax.applicativeError._ // for raiseError etc
  e.raiseError[Try, Int] // scala.util.Try[Int] = Failure(java.lang.RuntimeException: It's all gone wrong)


//  MonadError[Try, Int]

}
