package com.github.windbird123.cats.chap7

object FoldTest extends App {
  List(1, 2, 3).foldLeft(0)(_ - _) // -6
  List(1, 2, 3).foldRight(0)(_ - _) // 2

  List(1, 2, 3).foldLeft(List.empty[Int])((a, i) => i :: a) // List(3, 2, 1)
  List(1, 2, 3).foldRight(List.empty[Int])((i, a) => i :: a) // List(1, 2, 3)
}

object ExerciseFold extends App {
  def map[A, B](list: List[A])(f: A => B): List[B] =
    list.foldRight(List.empty[B])((i, a) => f(i) :: a)

//  def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
//    list.foldRight(List.empty[B])((i, a) => f(i) ::: a)
//
//  def flatMap[A](list: List[A])(f: A => Boolean): List[A] =
//    list.foldRight(List.empty[A])((i, a) => if (f(i)) i :: a else a)

  import cats.Monoid
  def sum[A](list: List[A])(implicit monoid: Monoid[A]): A =
    list.foldRight(monoid.empty)(monoid.combine)
}

