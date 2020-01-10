package com.github.windbird123.cats.chap2.second

case class Order(totalCost: Double, quantity: Double) extends Monoid[Any]

object CatsMonoidTest extends App {

  import cats.Monoid
  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.semigroup._
  import cats.syntax.option._

  def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  println(add(List(1.some, 2.some, 3.some)))

  val order1 = Order(1.1, 2.2)
  val order2 = Order(3.3, 4.4)

  implicit val monoidOrder: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0.0, 0.0)
    override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  }

  val out = add[Order](List(order1, order2))
  println(out)
}
