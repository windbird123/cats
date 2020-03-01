package com.github.windbird123.cats.chap01

// cats Eq 는 타입까지 같을 때 같다고 판독해 줌
object CatsEqTest {
  def main(args: Array[String]): Unit = {
    import cats.Eq
    import cats.instances.int._

    Eq[Int].eqv(123, 123) // true

    import cats.syntax.eq._
    123 === 123 // true
    123 =!= 456 // false

    import cats.instances.option._
    import cats.syntax.option._
    1.some === none[Int] // false
    1.some =!= none[Int] // true
  }
}

// Dog class 에 대한 eq 를 정의해 보자
final case class Dog(name: String, age: Int)

object DogExercise {
  def main(args: Array[String]): Unit = {
    import cats.Eq

    implicit val dogInst: Eq[Dog] = new Eq[Dog] {
      import cats.instances.int._
      import cats.instances.string._
      import cats.syntax.eq._
      override def eqv(x: Dog, y: Dog): Boolean =
        x.name === y.name && x.age === y.age
    }

    import cats.syntax.eq._
    Dog("a", 1) === Dog("a", 1) // true
  }
}
