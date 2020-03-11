package com.github.windbird123.cats.chap10

import cats.Semigroup
import cats.instances.list._
import cats.syntax.either._
import cats.syntax.semigroup._

sealed trait CheckE[E, A] {
  def and(that: CheckE[E, A]): CheckE[E, A] = AndE(this, that)

  def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] = this match {
    case PureE(func) => func(a)

    case AndE(left, right) =>
      (left(a), right(a)) match {
        case (Left(e1), Left(e2))   => (e1 |+| e2).asLeft
        case (Left(e), Right(a))    => e.asLeft
        case (Right(a), Left(e))    => e.asLeft
        case (Right(a1), Right(a2)) => a.asRight
      }

  }
}

final case class PureE[E, A](func: A => Either[E, A]) extends CheckE[E, A]

final case class AndE[E, A](left: CheckE[E, A], right: CheckE[E, A])
    extends CheckE[E, A]

object ADT_Either_2 {
  def main(args: Array[String]): Unit = {
    val a: CheckE[List[String], Int] = PureE { v =>
      if (v > 2) v.asRight
      else List("Must be > 2").asLeft
    }

    val b: CheckE[List[String], Int] = PureE { v =>
      if (v < -2) v.asRight
      else List("Must be < -2").asLeft
    }

    val check: CheckE[List[String], Int] = a and b
    check(0)
  }
}
