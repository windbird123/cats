package com.github.windbird123.cats.chap02

object CatsMonoidTest {
  def main(args: Array[String]): Unit = {
    import cats.Monoid
    import cats.instances.string._

    Monoid[String].combine("Hi", "there") // Hithere
    Monoid[String].empty // ""

    import cats.instances.int._
    import cats.instances.option._
    import cats.syntax.monoid._
    import cats.syntax.option._

    1.some |+| 2.some // Some(3)
    1.some |+| 2.some |+| none[Int] // Some(3)
  }
}

// 2.5.4 Exercise: Adding All The Things
object ExerciseAdd {
  /////////////////////////////////////////////
  // def add(items: List[Int]): Int 을 정의 하라
  /////////////////////////////////////////////

  import cats.Monoid
  import cats.instances.int._
  import cats.syntax.semigroup._ // for |+|
  def add(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)(_ |+| _)

  /////////////////////////////////////////////
  // def addOpt[A](items: List[A]): A 를 정의해 보자
  /////////////////////////////////////////////
  def addGeneral[A](items: List[A])(implicit m: Monoid[A]): A =
    items.foldLeft(m.empty)(_ |+| _)

  /////////////////////////////////////////////
  // def allAll[A](values: List[A])(implicit m: Monoid[A]): A 를 정의해 보자
  /////////////////////////////////////////////
  def addAll[A](values: List[A])(implicit m: Monoid[A]): A =
    values.foldRight(m.empty)(m.combine)

  // def add(items: List[Option[Int]]): Int 를 정의할려면?
  import cats.instances.option._
  import cats.syntax.option._
  addGeneral(List(1.some, 2.some)) // Some(3)

  /////////////////////////////////////////////
  // 아래 Order 에 대한 monoid 를 정의해 보자
  /////////////////////////////////////////////
  case class Order(totalCost: Double, quantity: Double)
  implicit val orderInst: Monoid[Order] = new Monoid[Order] {
    import cats.instances.double._
    override def empty: Order =
      Order(Monoid[Double].empty, Monoid[Double].empty)

    override def combine(x: Order, y: Order): Order =
      Order(x.totalCost |+| y.totalCost, x.quantity |+| y.quantity)
  }
}
