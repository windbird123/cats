package com.github.windbird123.cats.chap2.second

trait SemiGroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends SemiGroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
}


