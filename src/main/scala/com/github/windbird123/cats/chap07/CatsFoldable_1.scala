package com.github.windbird123.cats.chap07

object ScalaFoldableTest {
  def main(args: Array[String]): Unit = {
    List(1, 2, 3).foldLeft(0)(_ - _) // -6
    List(1, 2, 3).foldRight(0)(_ - _) // 2
    List(1, 2, 3).foldLeft(List.empty[Int])((acc, x) => x :: acc) // List(3, 2, 1)
    List(1, 2, 3).foldRight(List.empty[Int])((x, acc) => x :: acc) // List(1, 2, 3)
  }
}

object CatsFoldableTest {
  def main(args: Array[String]): Unit = {
    import cats.{Eval, Foldable}

    def bigData = (1 to 100).toStream

    import cats.instances.stream._ // for Foldable
    val eval: Eval[Long] = Foldable[Stream].foldRight(bigData, Eval.now(0L)) {
      (num, acc) =>
        acc.map(_ + num)
    }
    eval.value
  }
}

object MonoindFoldableTest {
  def main(args: Array[String]): Unit = {
    import cats.instances.int._
    import cats.instances.list._
    import cats.instances.string._
    import cats.syntax.foldable._

    List(1, 2, 3).combineAll // 6
    List(1, 2, 3).foldMap(_.toString) // 123
  }
}
