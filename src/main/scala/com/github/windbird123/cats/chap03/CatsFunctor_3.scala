package com.github.windbird123.cats.chap03

object CatsFunctorTest {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, 3, 4)

    import cats.Functor
    import cats.instances.list._
    Functor[List].map(list1)(_ * 2) // List(2, 4, 6)

    /////////////////////////////////////////////
    // Functor 의 lift 함수 사용해 보기
    /////////////////////////////////////////////
    val f = (x: Int) => x + 1

    import cats.instances.option._
    val liftF = Functor[Option].lift(f)

    import cats.syntax.option._
    liftF(1.some) // Some(2)
  }
}

object FunctorSyntaxTest {
  def main(args: Array[String]): Unit = {
    // Scala’s Function1 type doesn’t have a map method (it’s called andThen instead)
    import cats.instances.function._ // Function1 을 Functor 로 변환하기 위해
    import cats.syntax.functor._

    val f1 = (x: Int) => x + 1
    val f2 = (x: Int) => x * 2
    val f3 = (x: Int) => x + "!"
    val f4 = f1.map(f2).map(f3)

    f4(123) // 248!
  }
}
