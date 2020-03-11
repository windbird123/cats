package com.github.windbird123.cats.chap10

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated._
import cats.syntax.apply._
import cats.syntax.semigroup._ // for Valid and Invalid

sealed trait Predicate[E, A] {
  def and(that: Predicate[E, A]): Predicate[E, A] = AndP(this, that)
  def or(that: Predicate[E, A]): Predicate[E, A] = OrP(this, that)

  def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = this match {
    case PureP(func) => func(a)

    case AndP(left, right) => (left(a), right(a)).mapN((_, _) => a)

    case OrP(left, right) =>
      left(a) match {
        case Valid(a1) => Valid(a)
        case Invalid(e1) =>
          right(a) match {
            case Valid(a2)   => Valid(a)
            case Invalid(e2) => Invalid(e1 |+| e2)
          }
      }
  }
}

final case class AndP[E, A](left: Predicate[E, A], right: Predicate[E, A])
    extends Predicate[E, A]

final case class OrP[E, A](left: Predicate[E, A], right: Predicate[E, A])
    extends Predicate[E, A]

final case class PureP[E, A](func: A => Validated[E, A]) extends Predicate[E, A]

///////////////////////////////////////////////////////////////////////////////////////

sealed trait CheckQ[E, A, B] {
  def apply(in: A)(implicit s: Semigroup[E]): Validated[E, B]

  def map[C](f: B => C): CheckQ[E, A, C] = MapQ[E, A, B, C](this, f)
}

object CheckQ {
  def apply[E, A](pred: Predicate[E, A]): CheckQ[E, A, A] = PureQ(pred)
}

final case class MapQ[E, A, B, C](check: CheckQ[E, A, B], func: B => C)
    extends CheckQ[E, A, C] {
  def apply(in: A)(implicit s: Semigroup[E]): Validated[E, C] =
    check(in).map(func)
}

final case class PureQ[E, A](pred: Predicate[E, A]) extends CheckQ[E, A, A] {
  def apply(in: A)(implicit s: Semigroup[E]): Validated[E, A] = pred(in)
}
