package com.github.windbird123.cats.chap04

case class Cat(name: String, age: Int)

object CatsReaderTest {
  def main(args: Array[String]): Unit = {

    import cats.data.Reader
    val greetKitty: Reader[Cat, String] =
      Reader((cat: Cat) => cat.name).map(name => s"Hello $name")

    val feedKitty: Reader[Cat, String] = Reader(
      (cat: Cat) => s"good food for age: ${cat.age}"
    )

    val greetAndFeed: Reader[Cat, String] = for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed"

    greetAndFeed.run(Cat("windbird", 3)) // Hello windbird. good food for age: 3
  }
}

object ListReaderTest {
  def main(args: Array[String]): Unit = {
    import cats.data.Reader
    val reader1 = Reader[List[String], Int](list => list.size)
    val reader2 = Reader[List[String], Int](list => list.map(_.length).sum)

    val r = for {
      x <- reader1
      y <- reader2
    } yield (x, y)

    r.run(List("a", "ab", "abc")) // (3, 6)
  }
}

case class Db(userNames: Map[Int, String], passwords: Map[String, String])

object ExerciseReaderTest {
  import cats.data.Reader
  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.userNames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords.get(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      userName <- findUsername(userId)
      result <- userName
        .map(checkPassword(_, password))
        .getOrElse(Reader((_: Db) => false))
    } yield result
}
