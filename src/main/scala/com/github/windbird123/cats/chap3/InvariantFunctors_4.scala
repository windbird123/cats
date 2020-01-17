package com.github.windbird123.cats.chap3

trait Codec[A] {
  self =>

  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
    override def encode(value: B): String = self.encode(enc(value))

    override def decode(value: String): B = dec(self.decode(value))
  }
}

object Codec {
  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)
}

object CodecInstances {
  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(value: String): String = value

    override def decode(value: String): String = value
  }

  // 위의 stringCodec 을 활용해 Int, Boolean codec 을 정의
  implicit val intCodec: Codec[Int] = stringCodec.imap[Int](_.toInt, _.toString)

  implicit val booleanCodec: Codec[Boolean] =
    stringCodec.imap[Boolean](_.toBoolean, _.toString)

  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap[Double](_.toDouble, _.toString)
}

/////////////////////////////////////////////////////////////////////////////////
// 아래 Box2 class 에 대해 Codec 을 구현
/////////////////////////////////////////////////////////////////////////////////
//case class Box2[A](value: A)

object CodecTest extends App {
  implicit def box2Codec[A](implicit codec: Codec[A]): Codec[Box2[A]] =
    codec.imap[Box2[A]](Box2(_), _.value)

  import CodecInstances._
  val out = Codec.encode(Box2(true))
  println(out)

  val box = Codec.decode[Box2[Double]]("123.4")
  println(box)
}
