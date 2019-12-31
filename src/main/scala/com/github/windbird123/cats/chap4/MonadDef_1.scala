package com.github.windbird123.cats.chap4

import scala.language.higherKinds

trait MonadDef[F[_]] {
  def pure[A](value: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  // pure 와 flatMap 을 이용해 map 을 정의할 수 있다.
  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(a => pure(func(a)))
}
