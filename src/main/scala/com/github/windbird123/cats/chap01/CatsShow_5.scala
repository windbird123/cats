package com.github.windbird123.cats.chap01

object CatsShowTest {
  def main(args: Array[String]): Unit = {
    import cats.Show
    import cats.instances.int._
    Show[Int].show(123)

    import cats.syntax.show._
    123.show
  }
}

// java.util.Date 에 대한 show 를 만들어 보자
object DateShowTest {
  def main(args: Array[String]): Unit = {

    import java.util.Date

    import cats.Show
    implicit val dateInst: Show[Date] = new Show[Date] {
      override def show(t: Date): String = s"${t.getTime} ms since the epoch"
    }

    val date = new Date()
    import cats.syntax.show._
    date.show // 1582988585401 ms since the epoch
  }
}
