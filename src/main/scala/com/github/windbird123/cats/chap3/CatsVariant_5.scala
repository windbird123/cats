package com.github.windbird123.cats.chap3

trait Contravarint[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}

trait Invariant[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}

object CatsVariantTest extends App {
  import cats.Contravariant
  import cats.Show
  import cats.instances.string._

  val showString = Show[String]

  val showSymbol : Show[Symbol] = Contravariant[Show].contramap(showString)( (symbol: Symbol) => symbol.name + " KJM")
  val out = showSymbol.show('Dave)
  println(out)

  import cats.syntax.contravariant._
  val syntaxOut = showString.contramap((symbol: Symbol) => symbol.name)
}
