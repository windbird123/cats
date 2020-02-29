package com.github.windbird123.cats.chap01

trait Shape[A] {
  def area(a: A): Double
}

object Shape {
//  def area[A](a: A)(implicit shape: Shape[A]): Double = shape.area(a)
  def apply[A](implicit shape: Shape[A]): Shape[A] = shape

  implicit class ShapeSyntax[A](a: A) {
    def area()(implicit inst: Shape[A]): Double = inst.area(a)
  }
}

case class Circle(r: Int)
case class Rect(w: Int, h: Int)

object Circle {
  implicit val circleInst: Shape[Circle] = new Shape[Circle] {
    override def area(a: Circle): Double = Math.PI * a.r * a.r
  }
}

object Rect {
  implicit val rectInst: Shape[Rect] = new Shape[Rect] {
    override def area(a: Rect): Double = a.w * a.h
  }
}

object ShapeTest {
  def main(args: Array[String]): Unit = {
    // 방법 1
    val circle = Circle(2)
    Shape[Circle].area(circle) // 12.56

    // 방법 2
    import Shape._
    val rect = Rect(2, 3)
    rect.area() // 6.0
  }
}
