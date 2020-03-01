package com.github.windbird123.cats.chap03

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B) : F[B]

  // 아래 lift 를 위의 map 을 사용해 정의해 보자
  def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)
}

object FunctorLaws {
  // identity laws
  def identityLaw[F[_], A, B](implicit functor: Functor[F]) : Boolean = {
    val fa: F[A] = ???
    functor.map(fa)(x => x) == fa
    // fa.map(a => a) == fa
  }

  // composition
  // fa.map(g(f(_))) == fa.map(f).map(g)
}
