package com.github.windbird123.cats.chap01

sealed trait Json
final case class JsObject(map: Map[String, Json]) extends Json
final case class JsString(s: String) extends Json
final case class JsNumber(d: Double) extends Json
case object JsNull extends Json

trait JsonWriter[A] {
  def write(a: A): Json
}

object JsonWriter {
  def apply[A](implicit writer: JsonWriter[A]): JsonWriter[A] = writer

  implicit val mapInst: JsonWriter[Map[String, Json]] =
    new JsonWriter[Map[String, Json]] {
      override def write(a: Map[String, Json]): Json = JsObject(a)
    }

  implicit val stringInst: JsonWriter[String] = new JsonWriter[String] {
    override def write(a: String): Json = JsString(a)
  }

  implicit val doubleInst: JsonWriter[Double] = new JsonWriter[Double] {
    override def write(a: Double): Json = JsNumber(a)
  }

  implicit class JsonWriterSyntax[A](a: A) {
    def write(implicit writer: JsonWriter[A]): Json = writer.write(a)
  }

}

final case class Person(name: String, age: Int)

object Person {
  implicit val personInst: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(a: Person): Json = {
      val map = Map("name" -> JsString(a.name), "age" -> JsNumber(a.age))
      JsObject(map)
    }
  }
}

object PersonTest {
  def main(args: Array[String]): Unit = {
    val person = Person("kjm", 20)

    import JsonWriter._
    person.write // JsObject(Map(name -> JsString(kjm), age -> JsNumber(20.0)))

    implicitly[JsonWriter[Person]] // com.github.windbird123.cats.chap01.Person$$anon$4@13fee20c
  }
}
