package com.github.windbird123.cats.chap10

// type Check[E, A] = A => Either[E, A] 처럼 정의할 수도 있지만
// custom method 를 추가할 수 있도록 trait 으로 정의하자

trait CheckBasic[E, A] {
  def apply(value: A): Either[E, A]
  def and(that: CheckBasic[E, A]): CheckBasic[E, A]
}

// and 에서 두개 모두 체크하고 에러를 병합해 보도록 구현
import cats.Semigroup
import cats.syntax.either._
import cats.syntax.semigroup._ // for |+|

// 아래처럼 class class 를 정의하는 것은 매우 유용해 보인다 !!!
final case class CheckF[E, A](func: A => Either[E, A]) {
  def apply(a: A): Either[E, A] = func(a)

  def and(that: CheckF[E, A])(implicit s: Semigroup[E]): CheckF[E, A] = CheckF {
    a =>
      (this(a), that(a)) match {
        case (Left(e1), Left(e2))   => (e1 |+| e2).asLeft
        case (Left(e), Right(a))    => e.asLeft
        case (Right(a), Left(e))    => e.asLeft
        case (Right(a1), Right(a2)) => a.asRight
      }

  }
}

object DataValidationTest {
  def main(args: Array[String]): Unit = {
    import cats.instances.list._ // for Semigroup

    val a: CheckF[List[String], Int] = CheckF { v =>
      if (v > 2) v.asRight
      else List("Must be > 2").asLeft
    }

    val b: CheckF[List[String], Int] = CheckF { v =>
      if (v < -2) v.asRight
      else List("Must be < -2").asLeft
    }

    val check: CheckF[List[String], Int] = a and b
    check(5) // Left(List(Must be < -2))
    check(0) // Left(List(Must be > 2, Must be < -2))
  }
}
