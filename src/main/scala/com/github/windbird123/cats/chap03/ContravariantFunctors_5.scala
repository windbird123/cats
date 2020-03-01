package com.github.windbird123.cats.chap03

// The contramap method only makes sense for data types that represent transformations
// contramap 은 A type 에 대한 typeclass instance 로 부터 B type 에 대한 typeclass instance 를 만들어 내는데 사용하는 것 같다.
// boxPrintable2 에서 처럼 string 에 대한 typeclass instance 로 부터 Box[String] typeclass instance 를 만들어 낸다.
trait Printable2[A] {
  self =>
  def format(value: A): String

  def contamap[B](func: B => A): Printable2[B] = new Printable2[B] {
    override def format(value: B): String = self.format(func(value))
  }
}

object Printable2 {
  def format[A](value: A)(implicit p: Printable2[A]): String = p.format(value)
}

object Printable2Test extends App {
  implicit val stringPrintable2: Printable2[String] = new Printable2[String] {
    override def format(value: String): String = "\"" + value + "\""
  }

  implicit val booleanPrintale2: Printable2[Boolean] = new Printable2[Boolean] {
    override def format(value: Boolean): String = if (value) "yes" else "no"
  }

  val out = Printable2.format("hello")
  println(out)

  val out2 = Printable2.format(true)
  println(out2)
}

/////////////////////////////////////////////////////////////////////////////////
// 아래 Box class 에 대해 Printable2 를 구현해
// Printable2.format(box) 로 출력 가능하도록
/////////////////////////////////////////////////////////////////////////////////
case class Box[A](value: A)

object BoxTest extends App {
  implicit val stringPrintable2: Printable2[String] = new Printable2[String] {
    override def format(value: String): String = "\"" + value + "\""
  }

  implicit val booleanPrintale2: Printable2[Boolean] = new Printable2[Boolean] {
    override def format(value: Boolean): String = if (value) "yes" else "no"
  }

  // 방법 1
//  implicit def boxPrintable2[A](implicit p: Printable2[A]): Printable2[Box[A]] =
//    new Printable2[Box[A]] {
//      override def format(box: Box[A]): String = p.format(box.value)
//    }

  // 방법 2
  implicit def boxPrintable2[A](implicit p: Printable2[A]): Printable2[Box[A]] =
    p.contamap[Box[A]](box => box.value)

  val box = Box("hello world")
  val out = Printable2.format(box)
  println(out)
}
