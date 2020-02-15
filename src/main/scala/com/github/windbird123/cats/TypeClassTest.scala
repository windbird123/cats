package com.github.windbird123.cats

trait Shape[A] {
  def area(a: A): Double
}

object Shape {
  def apply[A](implicit inst: Shape[A]): Shape[A] = inst
//  def area[A](a: A)(implicit inst: Shape[A]): Double = inst.area(a)
}

case class Circle(r: Double)
case class Rect(w: Double, h: Double)

object ShapeInstances {
  implicit val circleInst: Shape[Circle] = new Shape[Circle] {
    override def area(circle: Circle): Double = Math.PI * circle.r * circle.r
  }

  implicit val rectInst: Shape[Rect] = new Shape[Rect] {
    override def area(rect: Rect): Double = rect.w * rect.h
  }
}

object ShapeSyntax {
  implicit class ShapeSyntaxImpl[A](any: A) {
    def area(implicit inst: Shape[A]): Double = Shape[A].area(any)
  }
}

object TypeClassTest extends App {
  import ShapeInstances._
  import ShapeSyntax._

  val circle = Circle(5)
  println(Shape[Circle].area(circle))
  println(circle.area)

  val rect = Rect(3, 4)
  println(Shape[Rect].area(rect))
  println(rect.area)
}
