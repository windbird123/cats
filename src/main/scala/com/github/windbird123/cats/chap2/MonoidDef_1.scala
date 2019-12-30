package com.github.windbird123.cats.chap2

/////////////////////////////////////////////////////////////////////////////////
// Monoid 정의
// 1. binary operation with associative law
// 2. identity
/////////////////////////////////////////////////////////////////////////////////
trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

object MonoidConditionTest {
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
    // (x * y) * z == x * (y * z)
    val xy_z = m.combine(m.combine(x, y), z)
    val x_yz = m.combine(x, m.combine(y, z))

    xy_z == x_yz
  }

  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
    // x * identity = x
    m.combine(x, m.empty) == x

    // identity * x = x
    m.combine(m.empty, x) == x
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Semigroup 정의
// Monoid 에서 identity 가 없는 것
/////////////////////////////////////////////////////////////////////////////////
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait MonoidFromSemigroup[A] extends Semigroup[A] {
  def empty: A
}



