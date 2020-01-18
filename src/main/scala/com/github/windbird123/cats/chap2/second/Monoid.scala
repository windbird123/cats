package com.github.windbird123.cats.chap2.second

import cats.kernel.Semigroup

trait SemiGroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends SemiGroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
}

object Test extends App {
  import cats.implicits._

  val out = Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
  println(out)
}


