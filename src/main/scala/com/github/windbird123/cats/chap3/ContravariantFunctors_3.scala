package com.github.windbird123.cats.chap3

// The contramap method only makes sense for data types that represent transformations
object ContravariantFunctorsTest {}

trait Printable2[A] {
  self =>

  def format(value: A): String

  def contramap[B](func: B => A): Printable2[B] = new Printable2[B] {
    override def format(value: B): String = self.format(func(value))
  }
}

object Printable2 {
  def format[A](value: A)(implicit p: Printable2[A]): String = p.format(value)
}
