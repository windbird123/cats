package com.github.windbird123.cats.chap1.all



trait Printable[A] {
  def format(a: A): String
}

final case class Cat(name: String, age: Int)

object Printable {
  def toFormat[A](a: A)(implicit p: Printable[A]): String = p.format(a)
}

object PrintableInstances {
  implicit class SuSyntax[A](a: A) {
    def toFormat(implicit p: Printable[A]) : String = p.format(a)
  }

  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(a: String): String = a
  }

  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }

  implicit val catPrintable: Printable[Cat] = new Printable[Cat] {
    override def format(cat: Cat): String = s"name: ${cat.name.toFormat}, age: ${cat.age.toFormat}"
  }
}


object PrintableTest extends App {
  import PrintableInstances._

  val cat = Cat("myCat", 11)
  val out = cat.toFormat
  println(out)
}
