package com.github.windbird123.cats.chap06

object CatsValidatedTest {
  def main(args: Array[String]): Unit = {
    import cats.instances.list._
    import cats.instances.string._
    import cats.syntax.apply._
    import cats.syntax.validated._

    ("error1".invalid[Int], "error2".invalid[Int]).tupled // Invalid(error1error2)

    ("error1".invalid[Int], "error2".invalid[Int])
      .mapN((_, _) => "X") // Invalid(error1error2)

    (List("error1, error2").invalid[Int], List("error3, error4").invalid[Int]).tupled // Invalid(List(error1, error2, error3, error4)), monoid 합성?
  }
}
