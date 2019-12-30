package com.github.windbird123.cats.chap3


trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B) : F[B]
}

// Functor map 은 다음 조건을 만족해야 한다.
// 1. identity:  fa.map(a => a) == fa
// 2. composition:  fa.map(g(f(_))) == fa.map(f).map(g)
