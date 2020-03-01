package com.github.windbird123.cats.chap04

trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  // Functor 의 map 을 위의 flatMap 을 사용해 정의하라
  def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(x => pure(f(x)))
}

object MoandLaws {
  // pure(a).flatMap(func) == func(a) // Left identity
  // m.flatMap(pure) == m // Right identity
  // m.flatMap(f).flatMap(g) // Associativity
}
