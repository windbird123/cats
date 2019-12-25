package com.github.windbird123.cats.chap1

// 1.3 Exercise
trait Printable[A] {
  def format(a: A): String
}

object Printable {
  def format[A](a: A)(implicit printable: Printable[A]): String =
    printable.format(a)
  def print[A](a: A)(implicit printable: Printable[A]): Unit =
    println(format(a))
}

object PrintableInstances {
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(a: String): String = a
  }

  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }

  implicit val catPrintable: Printable[Cat] = new Printable[Cat] {
    override def format(cat: Cat): String =
      s"NAME ${cat.name} AGE ${cat.age} COLOR ${cat.color}"
  }
}

final case class Cat(name: String, age: Int, color: String)

object PrintableTest {
  import PrintableInstances._

  def main(args: Array[String]): Unit = {
    val cat = Cat("A", 11, "blue")
    Printable.print(cat)
  }
}


/////////////////////////////////////////////////////////////////////////////////
// JsonSyntax 처럼 implicit class 를 만들어 사용하기 편하게 개선해 보자
// cat.print() 처럼..
/////////////////////////////////////////////////////////////////////////////////

object PrintableSyntax {
  implicit class PrintableOps[A](a: A) {
    def format(implicit printable: Printable[A]) : String = printable.format(a)

    def print(implicit printable: Printable[A]): Unit = println(format)
  }
}

object PrintableSyntaxTest {
  def main(args: Array[String]): Unit = {
    import PrintableSyntax._
    import PrintableInstances._

    val cat = Cat("A", 11, "blue")
    cat.print
  }
}


