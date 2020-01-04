package com.github.windbird123.cats.chap2

object MonoidInstnaceTest {
  def main(args: Array[String]): Unit = {
    import cats.Monoid
    import cats.Semigroup
    import cats.instances.string._

    Monoid[String]
      .combine("Hi ", "there") // equivalent to Monoid.apply[String].combin(...)
    Semigroup[String].combine("Hi ", "there")

    val a = Option(22)
    val b = Option(20)

    import cats.instances.option._
    import cats.instances.int._
    Monoid[Option[Int]].combine(a, b) // Some(42)
  }
}

object MonoidSyntaxTest extends App {
  import cats.Monoid
  import cats.instances.string._
  import cats.syntax.semigroup._

  val stringResult = "Hi " |+| "there" |+| Monoid[String].empty // Hi there

  import cats.instances.int._
  val intResult = 1 |+| 2 |+| Monoid[Int].empty // 3
}

case class Order(totalCost: Double, quantity: Double)

object SuperAdderTest {
  import cats.Monoid
  import cats.instances.int._
  import cats.syntax.semigroup._

  def add(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)(_ |+| _)

  def addSuper[A](items: List[A])(implicit m: Monoid[A]): A =
    items.foldLeft(m.empty)(_ |+| _)

  def main(args: Array[String]): Unit = {
    val list = List(Some(1), Some(2), None, Some(3)) // None 이 하나라도 끼어있어야 Option 타입으로 인식, 아니면 Some 타입..

    import cats.instances.option._
    val out = addSuper(list)
    println(out)

    // custom type 의 Order 에 적용해 보자
    val orders = List(Order(2.1, 1.1), Order(3.3, 4.3))

    implicit val monoidOrder: Monoid[Order] = new Monoid[Order] {
      override def empty: Order = Order(0.0, 0.0)
      override def combine(x: Order, y: Order): Order =
        Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
    }

    val out2 = addSuper(orders)
    println(out2)
  }

}
