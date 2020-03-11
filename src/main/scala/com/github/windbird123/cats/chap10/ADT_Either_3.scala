package com.github.windbird123.cats.chap10

import cats.Semigroup
import cats.data.Validated
import cats.syntax.semigroup._ // for |+|
import cats.data.Validated.{Invalid, Valid}
import cats.syntax.apply._ // for mapN

sealed trait Check[E, A] {
  def and(that: Check[E, A]): Check[E, A] = And(this, that)

  def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = this match {
    case Pure(func)       => func(a)
    case And(left, right) => (left(a), right(a)).mapN((_, _) => a)
    case Or(left, right)  => left(a)  match {
      case Valid(a) => Valid(a)
      case Invalid(e1) => right(a) match {
        case Valid(a) => Valid(a)
        case Invalid(e2) => Invalid(e1 |+| e2)
      }
    }
  }
}

final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
final case class Or[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
final case class Pure[E, A](func: A => Validated[E, A]) extends Check[E, A]

object ADT_Either_3 {
  def main(args: Array[String]): Unit = {}

}
