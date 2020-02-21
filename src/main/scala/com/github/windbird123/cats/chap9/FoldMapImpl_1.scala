package com.github.windbird123.cats.chap9

object FoldMapImpl {

  import cats.Monoid
  import cats.syntax.monoid._

  def foldMap[A, B: Monoid](v: Vector[A])(f: A => B): B =
    v.foldLeft(Monoid[B].empty) { (b, a) =>
      b |+| f(a)
    }

  import cats.instances.future._
  import cats.instances.vector._
  import cats.syntax.foldable._
  import cats.syntax.monoid._
  import cats.syntax.traverse._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def parallelFoldMap[A, B: Monoid](values: Vector[A])(f: A => B): Future[B] =
    values
      .grouped(3)
      .toVector
      .traverse { v =>
        Future { v.foldLeft(Monoid[B].empty)(_ |+| f(_)) }
      }
      .map(_.combineAll)

}
