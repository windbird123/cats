package com.github.windbird123.cats.chap1

// 1.4 Meet Cats

/////////////////////////////////////////////////////////////////////////////////
// cats 의 Show 는 앞의 Printable 과 유사한 기능을 제공한다.
// cats library 를 사용해 보자
/////////////////////////////////////////////////////////////////////////////////
object CatsShow {

  def main(args: Array[String]): Unit = {
    import cats.Show
    import cats.instances.int._
    import cats.instances.string._

    val showInt: Show[Int] = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]

    val intAsString: String = showInt.show(123)
    println(intAsString)

    val stringAsString: String = showString.show("abc")
    println(stringAsString)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// JsonSyntax / PrintableSyntax 처럼 쉬운 interface 를 사용해 보자
/////////////////////////////////////////////////////////////////////////////////
object ShowSyntax {
  def main(args: Array[String]): Unit = {
    import cats.instances.int._
    import cats.instances.string._
    import cats.syntax.show._

    val showInt = 123.show
    println(showInt)

    val showString = "abc".show
    println(showString)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Show 사용을 위해 type class, type class instance & syntax 를 import 각각 import 했는데
// 다음과 같이 cats 의 모든 것을 import 할 수도 있다.
// import cats._
// import cats.implicits._
/////////////////////////////////////////////////////////////////////////////////
object AllImportTest {
  def main(args: Array[String]): Unit = {
    // import cats._  는 imports all of Cats’ type classes in one go
    import cats.implicits._ // imports all of the standard type class instances and all of the syntax in one go

    val showInt = 123.show
    println(showInt)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// cats 에서 제공해 주는 default show instance 대신에 custom instance 를 만들어 사용할 수 있다.
// java.util.Date 를 출력하는 custom 을 만들어 보자
/////////////////////////////////////////////////////////////////////////////////
object CustomDateShow {
  import java.util.Date

  import cats.Show

  implicit val dateShow: Show[Date] = new Show[Date] {
    override def show(date: Date): String = s"${date.getTime}ms since the epoch"
  }
}

object CustomDateShowTest {
  def main(args: Array[String]): Unit = {
    import java.util.Date

    import CustomDateShow._
    import cats.implicits._

    val showDate = new Date().show
    println(showDate)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// 위에서 dateShow 를 만들어 했던 작업을, 좀 더 간단하게 할 수 있다.
/////////////////////////////////////////////////////////////////////////////////
object CustomDateShowSimple {
  import java.util.Date

  import cats.Show

  implicit val dateShow: Show[Date] =
    Show.show(date => s"${date.getTime}ms since the epoch")
}

object CustomDateShowSimpleTest {
  def main(args: Array[String]): Unit = {
    import java.util.Date

    import CustomDateShowSimple._
    import cats.implicits._

    val showDate = new Date().show
    println(showDate)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Printable 에서 했던 작업을 cats 의 Show library 를 활용해 구현해 보자
/////////////////////////////////////////////////////////////////////////////////
object CatsPrintable {
  import cats.Show
  implicit val catShow: Show[Cat] =
    Show.show[Cat](cat => s"NAME ${cat.name} AGE ${cat.age} COLOR ${cat.color}")
}

object CatsPrintableTest {
  def main(args: Array[String]): Unit = {
    import CatsPrintable._
    import cats.implicits._

    val cat = Cat("A", 11, "blue")
    val showCat = cat.show
    println(showCat)
  }
}
