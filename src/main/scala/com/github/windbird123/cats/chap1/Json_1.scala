package com.github.windbird123.cats.chap1

// 1.1 Type Class
sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(
      Map("name" -> JsString(value.name), "email" -> JsString(value.email))
    )
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Interface Object
/////////////////////////////////////////////////////////////////////////////////
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

object JsonTest {
  def main(args: Array[String]): Unit = {
    import JsonWriterInstances._
    val json = Json.toJson(Person("Dave", "dave@example.com"))
    println(json)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// Interface Syntax
/////////////////////////////////////////////////////////////////////////////////
object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }
}

object JsonSyntaxTest {
  import JsonSyntax._
  import JsonWriterInstances._

  def main(args: Array[String]): Unit = {
    val person = Person("Dave", "dave@example.com")
    val json = person.toJson
    println(json)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// We can use implicitly to summon any value from implicit scope.
/////////////////////////////////////////////////////////////////////////////////
object ImplicitlyTest {
  def main(args: Array[String]): Unit = {
    import JsonWriterInstances._

    val w = implicitly[JsonWriter[String]]
    println(w)
  }
}

/////////////////////////////////////////////////////////////////////////////////
// JsonWriter[A] 를 구현했는데, JsonWriter[Option[A]] 가 필요하다면? (Recursive Implicit Resolution)
/////////////////////////////////////////////////////////////////////////////////
object RecursiveImplicit {
  implicit def optionWriter[A](
    implicit writer: JsonWriter[A]
  ): JsonWriter[Option[A]] = new JsonWriter[Option[A]] {
    override def write(option: Option[A]): Json = option match {
      case Some(aValue) => writer.write(aValue)
      case None         => JsNull
    }
  }
}

object RecursiveImplicitTest {
  import JsonWriterInstances._
  import RecursiveImplicit._

  def main(args: Array[String]): Unit = {
    val json = Json.toJson(Option("A string"))
    println(json)
  }
}

// implicit def 대신에 JsonSyntax 에서 처럼 implicit class 를 쓸 수 도 있지 않을까?
// https://stackoverflow.com/questions/36432376/implicit-class-vs-implicit-conversion-to-trait
object RecursiveImplicitClass {
  implicit class JsonOptionClazz[A](option: Option[A]) {
    def toJson(implicit writer: JsonWriter[A]): Json = option match {
      case Some(aValue) => writer.write(aValue)
      case None         => JsNull
    }
  }
}

object RecursiveImplicitClassTest {
  import JsonWriterInstances._
  import RecursiveImplicitClass._

  def main(args: Array[String]): Unit = {
    val json = Option("A string").toJson
    println(json)
  }
}
