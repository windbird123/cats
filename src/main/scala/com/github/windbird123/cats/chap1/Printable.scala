package com.github.windbird123.cats.chap1

trait Printable[A] {
  def format(a: A): String
}

object Printable {
  def format[A](a: A)(implicit printable: Printable[A]): String =
    printable.format(a)
  def print[A](a: A)(implicit printable: Printable[A]): Unit =
    println(format(a))
}

object PritnableInstances {
  implicit val stringPrintable = new Printable[String] {
    override def format(a: String): String = a
  }

  implicit val intPrintable = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }

  implicit val catPrintable = new Printable[Cat] {
    override def format(cat: Cat): String =
      s"NAME ${cat.name} AGE ${cat.age} COLOR ${cat.color}"
  }
}

final case class Cat(name: String, age: Int, color: String)

object PrintableTest {
  import PritnableInstances._

  def main(args: Array[String]): Unit = {
    val cat = Cat("A", 11, "blue")
    Printable.print(cat)
  }
}
