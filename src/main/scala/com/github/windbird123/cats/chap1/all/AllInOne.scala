package com.github.windbird123.cats.chap1.all

sealed trait Json

final case class JsObject(map: Map[String, Json]) extends Json
final case class JsString(s: String) extends Json
final case class JsNumber(n: Int) extends Json
case object JsNull extends Json

trait JsWriter[A] {
  def write(value: A): Json
}

object Json {
  def toJson[A](value: A)(implicit w: JsWriter[A]): Json = w.write(value)
}

object WriterInstances {
  implicit val objInst: JsWriter[Map[String, Json]] =
    new JsWriter[Map[String, Json]] {
      override def write(value: Map[String, Json]): Json = JsObject(value)
    }

  implicit val stringInst: JsWriter[String] = new JsWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val numberInst: JsWriter[Int] = new JsWriter[Int] {
    override def write(value: Int): Json = JsNumber(value)
  }

  implicit val personInst: JsWriter[Person] = new JsWriter[Person] {
    override def write(value: Person): Json = JsObject(
      Map("name" -> JsString(value.name), "age" -> JsNumber(value.age))
    )
  }

  implicit def optInst[A](implicit w: JsWriter[A]): JsWriter[Option[A]] =
    new JsWriter[Option[A]] {
      override def write(value: Option[A]): Json = value match {
        case Some(a) => Json.toJson(a)
        case None    => JsNull
      }
    }
}

case class Person(name: String, age: Int)

object JsonTest extends App {
  implicit class SugarSyntax[A](a: A) {
    def toJson(implicit w: JsWriter[A]): Json = w.write(a)
  }

  import WriterInstances._
  val out = Option(Person("kjm", 20)).toJson

  println(out)
}
