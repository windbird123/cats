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

    // implicit instance 를 명시적으로 지정해 사용할 때는 아래와 같이 할 수 있다.
    // (myRectInst, myRectInst2 가 있어 골라 사용해야 한다면..)
    MyShape[MyRect](MyRect.myRectInst).area(rect)


    // context bound 에 관한 설명은 TypeClass_1 을 참고할 것 !!!
  }
}
