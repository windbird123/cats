package com.github.windbird123.cats.chap9



object FoldMapImpl {

  import cats.Monoid
  import cats.syntax.monoid._

  def foldMap[A, B: Monoid](seq : Vector[A])(f: A => B): B = seq.map(f).foldLeft(Monoid[B].empty)( _ |+| _)
}
