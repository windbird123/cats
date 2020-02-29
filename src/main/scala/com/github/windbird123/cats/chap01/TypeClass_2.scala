package com.github.windbird123.cats.chap01

import simulacrum.typeclass

// simulacrum 라이브러리를 이용해 type class 정의
// sbt scalacOptions 에  "-language:implicitConversions" 가 추가 되어야 한다.

@typeclass trait MyShape[A] {
  def area(a: A): Double
}

case class MyRect(w: Int, h: Int)

object MyRect {
  implicit val myRectInst: MyShape[MyRect] = new MyShape[MyRect] {
    override def area(a: MyRect): Double = a.w * a.h
  }
}

object MyShapeTest {
  def main(args: Array[String]): Unit = {
    val rect = MyRect(2, 3)

    import MyShape.ops._
    rect.area // 6.0
  }
}
