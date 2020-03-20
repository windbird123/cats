package com.github.windbird123.cats.chap10.recap

import cats.Semigroup
import cats.data.Validated // for valid and invalid

sealed trait Check[E, A, B] {
  import Check._

  def apply(in: A)(implicit s: Semigroup[E]): Validated[E, B]

  def map[C](f: B => C): Check[E, A, C] = Map[E, A, B, C](this, f)

  def flatMap[C](f: B => Check[E, A, C]) = FlatMap[E, A, B, C](this, f)

  def andThen[C](next: Check[E, B, C]): Check[E, A, C] =
    AndThen[E, A, B, C](this, next)
}

object Check {
  final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C)
      extends Check[E, A, C] {

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] =
      check(a) map func
  }

  final case class FlatMap[E, A, B, C](check: Check[E, A, B],
                                       func: B => Check[E, A, C])
      extends Check[E, A, C] {

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] =
      check(a).withEither(_.flatMap(b => func(b)(a).toEither))
  }

  final case class AndThen[E, A, B, C](check: Check[E, A, B],
                                       next: Check[E, B, C])
      extends Check[E, A, C] {

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] =
      check(a).withEither(_.flatMap(b => next(b).toEither))
  }

  final case class Pure[E, A, B](func: A => Validated[E, B])
      extends Check[E, A, B] {

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, B] =
      func(a)
  }

  final case class PurePredicate[E, A](pred: Predicate[E, A])
      extends Check[E, A, A] {

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
      pred(a)
  }

  def apply[E, A](pred: Predicate[E, A]): Check[E, A, A] =
    PurePredicate(pred)

  def apply[E, A, B](func: A => Validated[E, B]): Check[E, A, B] =
    Pure(func)
}

final case class User(username: String, email: String)

object CheckTest {
  import cats.data.{NonEmptyList, Validated}
  import cats.syntax.apply._
  import cats.syntax.validated._
  type Errors = NonEmptyList[String]

  def error(s: String): NonEmptyList[String] =
    NonEmptyList(s, Nil)

  def longerThan(n: Int): Predicate[Errors, String] =
    Predicate.lift(
      error(s"Must be longer than $n characters"),
      str => str.length > n
    )

  val alphanumeric: Predicate[Errors, String] =
    Predicate.lift(
      error(s"Must be all alphanumeric characters"),
      str => str.forall(_.isLetterOrDigit)
    )

  def contains(char: Char): Predicate[Errors, String] =
    Predicate.lift(
      error(s"Must contain the character $char"),
      str => str.contains(char)
    )

  val checkUsername: Check[Errors, String, String] =
    Check(longerThan(3) and alphanumeric)

  val splitEmail: Check[Errors, String, (String, String)] =
    Check(_.split('@') match {
      case Array(name, domain) =>
        (name, domain).validNel[String]

      case _ =>
        "Must contain a single @ character".invalidNel[(String, String)]
    })

  val checkLeft: Check[Errors, String, String] =
    Check(longerThan(0))

  val checkRight: Check[Errors, String, String] =
    Check(longerThan(3) and contains('.'))

  val joinEmail: Check[Errors, (String, String), String] =
    Check { x =>
      x match {
        case (l, r) =>
          (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
      }
    }

  val checkEmail
    : Check[Errors, String, String] = splitEmail andThen joinEmail // for mapN

  def createUser(username: String, email: String): Validated[Errors, User] =
    (checkUsername(username), checkEmail(email)).mapN(User)

  def main(args: Array[String]): Unit = { // for valid and invalid

    createUser("Noel", "noel@underscore.io")
    // res5: Validated[Errors, User] = Valid(User("Noel", "noel@underscore.io"))

    createUser("", "dave@underscore.io@io")
    // res6: Validated[Errors, User] = Invalid(
    //   NonEmptyList(
    //     "Must be longer than 3 characters",
    //     List("Must contain a single @ character")
    //   )
    // )
  }
}
