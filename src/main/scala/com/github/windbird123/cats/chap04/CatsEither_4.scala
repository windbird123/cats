package com.github.windbird123.cats.chap04

object CatsEitherTest {
  import cats.syntax.either._

  def main(args: Array[String]): Unit = {

    val a = 3.asRight[String]
    val b = 4.asRight[String]

    for {
      x <- a
      y <- b
    } yield x * x + y * y // Right(25)
  }
}

object WhyShouldUseCatsEither {
  import cats.syntax.either._

  def countPositive(nums: List[Int]): Either[String, Int] =
    nums.foldLeft(0.asRight[String]) { (acc, num) => // 0.asRight[String] 대신에 Right(0) 을 사용하면
      if (num > 0) { // acc 의 type 을 Right[Nothing, Int] 로 추정하게 된다.
        acc.map(_ + 1)
      } else {
        Left("Negative")
      }
    }

  // Try 이나 Option 으로 부터 Either 를 만들수 있다.
  Either.fromTry(util.Try("foo".toInt)) // Either[Throwable,Int]
  Either
    .fromOption[String, Int](None, "Badness") // Either[String,Int] = Left(Badness)
  Either.fromOption[String, Int](Some(3), "Badness")

  // transform Either
  "Error".asLeft[Int].getOrElse(0) // 0
  "Error".asLeft[Int].orElse(2.asRight[String]) // Right(2)
  (-1)
    .asRight[String]
    .ensure(("Must be non-negative"))(_ > 0) // Either[String,Int] = Left(Must be non-negative!)

  // recover
  "error".asLeft[Int].recover { case _: String     => -1 }
  "error".asLeft[Int].recoverWith { case _: String => (-1).asRight }

  // leftMap, biMap, swap
  "foo".asLeft[Int].leftMap(_.reverse) // Left(oof)
  6.asRight[String].bimap(_.reverse, _ * 7) // Right(42)
  123.asRight[String].swap // Left(123)

  123.asRight[String].toValidated
  123.asRight[String].toList
}
