package com.github.windbird123.cats.chap01

// 1.3 Exercise: Printable Library

trait Printable[A] {
  def format(a: A): String
}

object Printable {
  def apply[A](implicit p: Printable[A]): Printable[A] = p

  implicit class PrintableSyntax[A](a: A) {
    def format(implicit p: Printable[A]): String = p.format(a)
  }
}

final case class Cat(name: String, age: Int)
object Cat {
  implicit val catInst: Printable[Cat] = new Printable[Cat] {
    override def format(a: Cat): String = s"name: ${a.name}, age: ${a.age}"
  }
}

object PrintableTest {
  def main(args: Array[String]): Unit = {
    val cat = Cat("nayang", 5)
    Printable[Cat].format(cat) // name: nayang, age: 5

    import Printable._
    cat.format // // name: nayang, age: 5
  }
}
