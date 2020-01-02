package com.github.windbird123.cats.chap3

object CatsFunctorTest {
  import cats.Functor
  import cats.instances.list._
  import cats.instances.option._

  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, 3)
    Functor[List].map(list1)(_ * 2)

    val option1 = Option(123)
    Functor[Option].map(option1)(_.toString)
  }
}

object FunctorSyntaxTest {
  // Functor 에 정의된 map 과 List(혹은 Option) 의 scala 기본 제공 map 과 충돌로 ..
  // Scala’s Function1 type doesn’t have a map method (it’s called andThen instead) so there are no naming conflicts

  import cats.instances.function._
  import cats.syntax.functor._

  def main(args: Array[String]): Unit = {
    val func1 = (a: Int) => a + 1
    val func2 = (a: Int) => a + 2
    val func3 = (a: Int) => a + "!"

    // scalacOptions += "-Ypartial-unification" 필요
    val func4 = func1.map(func2).map(func3) // scala 의 andThen 대신에 cats 의 map 을 사용
    func4(123)
  }
}

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)
}

object BranchTest extends App {
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = tree match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value)         => Leaf(f(value))
    }
  }

  implicit class FunctorOps[F[_], A](src: F[A]) {
    def map[B](func: A => B)(implicit functor: Functor[F]): F[B] =
      functor.map(src)(func)
  }

  val b: Branch[Int] = Branch(Leaf(10), Leaf(20))
  // 아래 코드는 동작 하지 않음 - implicit Functor[Branch] 를 찾기 때문 ...
  //  b.map(_ * 2)

  //  val b: Tree[Int] = Branch(Leaf(10), Leaf(20))  로 하면
  //  b.map(_* 2) 가 working 한다.  (위의 FunctorOps 가 반드시 필요)
}
