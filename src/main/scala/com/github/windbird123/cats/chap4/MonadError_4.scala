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

  import cats.instances.try_._

  import scala.util.Try
//  import cats.instances.either._
//  import cats.instances.int._

  val e: Throwable = new RuntimeException("It's all gone")

  import cats.syntax.applicativeError._ // for raiseError etc
  e.raiseError[Try, Int] // scala.util.Try[Int] = Failure(java.lang.RuntimeException: It's all gone wrong)

//  MonadError[Try, Int]

}

object TryMonadErrorTest extends App {
  import cats.MonadError
  import cats.instances.future._
  import cats.instances.try_._
  import cats.syntax.applicativeError._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import scala.util.Try

  def parseUser[F[_]](
    s: String
  )(implicit me: MonadError[F, Throwable]): F[Int] = s match {
    case "1" => me.pure(1)
    case "2" => me.raiseError(new Exception("2"))
    case "3" =>
      // fromTry 보다는 아래처럼 catchNonFatal 또는 catchNonFatalEval 을 사용하자
      me.fromTry {
        Try {
          1 + 2
        }
      }

    case _ =>
      me.catchNonFatal[Int] {
          throw new Exception("4")
        }
        .recoverWith {
          case ex => me.pure(4)
        }
  }

  val user = parseUser[Try]("3")
  println(user)

  val user2 = parseUser[Future]("4")
  println(user2)
}
