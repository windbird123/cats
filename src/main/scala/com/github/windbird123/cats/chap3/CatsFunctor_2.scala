package com.github.windbird123.cats.chap3

object CatsFunctorTest {
  import cats.Functor
  import cats.instances.list._
  import cats.instances.option._

  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, 3)
    val list2 = Functor[List].map(list1)(_ * 2)

    val option1 = Option(123)
    val option2 = Functor[Option].map(option1)(_.toString)
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
//    val func4 = func1.map(func2).map(func3) // scala 의 andThen 대신에 cats 의 map 을 사용
//    func4(123)
  }

//  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]) : F[Int] = start.map(n => n + 1 * 2)
}


