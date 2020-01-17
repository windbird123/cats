package com.github.windbird123.cats

import scala.util.Try

trait Shape[A] {
  def area(a: A): Double
}

object Shape {
  def area[A](a: A)(implicit inst: Shape[A]): Double = inst.area(a)
}

case class Circle(r: Double)
case class Rect(w: Double, h: Double)

object ShapeInstances {
  implicit object circleInst extends Shape[Circle] {
    override def area(circle: Circle): Double = Math.PI * circle.r * circle.r
  }

  implicit object rectInst extends Shape[Rect] {
    override def area(rect: Rect): Double = rect.w * rect.h
  }
}

object ShapeSyntax {
  implicit class ShapeSyntaxImpl[A](any: A) {
    def area(implicit inst: Shape[A]): Double = Shape.area(any)
  }
}

object TypeClassTest extends App {
  import ShapeInstances._
  import ShapeSyntax._

  val circle = Circle(5)
  println(Shape.area(circle))
  println(circle.area)

  val rect = Rect(3, 4)
  println(Shape.area(rect))
  println(rect.area)
}

object TmpTest extends App {
  import cats.MonadError
  import cats.instances.try_._

  def parseUser[F[_]](
    s: String
  )(implicit me: MonadError[F, Throwable]): F[String] = {
    me.pure("abc")
//    me.raiseError("error")
  }

//  implicit val me = MonadError[Try, Throwable]
  val user = parseUser[Try]("abc")
  println(user)
}
