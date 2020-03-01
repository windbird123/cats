package com.github.windbird123.cats.chap02

// 여러가지 타입에 대한 Monoid
object CatsOtherMonoidTest {
  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.monoid._
  import cats.syntax.option._

  1.some |+| 2.some // Some(3)

  import cats.instances.map._
  Map("a" -> 1, "b" -> 2) |+| Map("b" -> 3, "d" -> 4) // Map(b -> 5, d -> 4, a -> 1)

  def main(args: Array[String]): Unit = {
    import cats.instances.int._
    import cats.instances.string._
    import cats.instances.tuple._
    import cats.syntax.monoid._

    val tuple1 = ("hello", 123)
    val tuple2 = ("world", 456)
    tuple1 |+| tuple2 // (helloworld,579)
  }
}
