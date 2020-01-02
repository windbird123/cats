package com.github.windbird123.cats.chap1

// 1.5 cats.Eq. Eq is designed to support type-safe equality and address annoyances using Scala’s built-in == operator.
object NormalScala {
  def main(args: Array[String]): Unit = {
    // 아래 코드는 Option 과 Int 를 비교하는 오류인데, compiler 에서 잡아주지 못한다.
//    List(1, 2, 3).map(Option(_)).filter(item => item == 1)
  }
}

object CatsIntEq {
  def main(args: Array[String]): Unit = {
    import cats.Eq
    import cats.instances.int._

    val eqInt = Eq.apply[Int]
    eqInt.eqv(123, 123)

    import cats.syntax.eq._
    123 === 123

    // 이제는 아래와 같이 다른 type 을 비교 했을 때, 이제는 compile error 발생
    // eqInt.eqv(123, "123")
  }
}

object CatsOptionEq {
  def main(args: Array[String]): Unit = {

    import cats.instances.int._
    import cats.instances.option._
    import cats.syntax.eq._

    // 아래와 같이 했을 때 compile error
    // Some(1) === None

    // 다음과 같이 해야 한다.
    (Some(1): Option[Int]) === (None: Option[Int])
    Option(1) === Option.empty[Int]

    // cats 는 좀 더 편한 interface 를 제공한다.
    import cats.syntax.option._
    1.some === none[Int] // false
    1.some =!= none[Int] // true
  }
}

object CustomEq {
  def main(args: Array[String]): Unit = {
    import java.util.Date

    import cats.Eq
    import cats.instances.long._
    import cats.syntax.eq._

    implicit val dateEq: Eq[Date] = Eq.instance[Date] { (date1, date2) =>
      date1.getTime === date2.getTime
    }

    val x = new Date()
    x === x // true
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Exercise
// Cat equality
/////////////////////////////////////////////////////////////////////////////////
object ExerciseCatEq {
  import cats.Eq
  import cats.instances.int._
  import cats.instances.string._
  import cats.syntax.eq._

  implicit val catEq: Eq[Cat] = Eq.instance[Cat](
    (cat1, cat2) => cat1.name === cat2.name && cat1.age === cat2.age
  )

}

object ExerciseCatEqTest {
  def main(args: Array[String]): Unit = {
    import ExerciseCatEq._
    import cats.syntax.eq._

    val cat1 = Cat("Garfield", 38, "orange and black")
    val cat2 = Cat("Heathcliff", 33, "orange and black")
    cat1 === cat2

    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]

    import cats.instances.option._
    optionCat1 =!= optionCat2
  }
}
