package com.github.windbird123.cats.chap4

import scala.util.Try

object ScalaEitherTest extends App {
  val either1: Either[String, Int] = Right(10)
  val either2: Either[String, Int] = Right(30)

  // scala 2.12+ 는 이게 가능
  for {
    a <- either1.right
    b <- either2.right
  } yield a + b

  // cats 는 scala 2.11 에서 쓸수 있도록 제공

}

object CatsEither extends App {
  import cats.syntax.either._

  val a: Either[String, Int] = 10.asRight[String]
  val b: Either[String, Int] = 30.asRight[String]

  for {
    x <- a
    y <- b
  } yield x * x + y * y

  // cats Either 는 아래와 같이 제공
  Either.catchOnly[NumberFormatException]("foo".toInt) // Either[NumberFormatException,Int]
  Either.catchNonFatal(sys.error("Bad")) // Either[Throwable,Nothing]

  Either.fromTry(Try("foo".toInt)) // Either[Throwable,Int]
  Either.fromOption[String, Int](None, "Bad") // Either[String,Int] = Left(Badness)

  "Error".asLeft[Int].getOrElse(0)
  (-1).asRight[String].ensure("Must be non-negative!")(_ > 0) // Either[String,Int] = Left(Must be non-negative!)
}

