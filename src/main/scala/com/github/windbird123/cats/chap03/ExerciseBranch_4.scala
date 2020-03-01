package com.github.windbird123.cats.chap03

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

// Tree 에 대해 Functor 를 작성하라
object ExerciseBranchTest {
  def main(args: Array[String]): Unit = {
    import cats.Functor
    implicit val treeInst: Functor[Tree] = new Functor[Tree] {
      override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(value)         => Leaf(f(value))
      }
    }

    import cats.syntax.functor._
    // Branch(Leaf(10), Leaf(20)).map(_ * 2)
    // compiler 가 Branch 에 대한 instance 를 찾기 때문에 에러가 난다. ==> Tree 에 대한 instance 를 찾도록 해야 한다.

    val tree: Tree[Int] = Branch(Leaf(10), Leaf(20))
    tree.map(_ * 2) // Branch(Leaf(20),Leaf(40))
  }

}
