package com.github.windbird123.cats

import simulacrum._

// sbt scalacOptions 에  "-language:implicitConversions" 가 추가 되어야 한다.
@typeclass trait Shp[A] {
  def area(a: A): Double
}

case class Rectangle(w: Double, h: Double)

object Rectangle {
  implicit val rectInst: Shp[Rectangle] = new Shp[Rectangle] {
    override def area(a: Rectangle): Double = a.w * a.h
  }
}

object TypeClassWithLib extends App {
  val rect = Rectangle(3, 4)
  import Shp.ops._

  println(Shp[Rectangle].area(rect))
  println(rect.area)
}
