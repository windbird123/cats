package com.github.windbird123.cats.chap4

import cats.data.Reader

case class Cat(name: String, favoriteFood: String)

object CatsReaderTest extends App {
  val greetKitty: Reader[Cat, String] =
    Reader[Cat, String](cat => cat.name).map(name => s"Hello $name")
  val feedKitty: Reader[Cat, String] = Reader(
    cat => s"nice food ${cat.favoriteFood}"
  )

  val greetAndFeed: Reader[Cat, String] = for {
    greet <- greetKitty
    feed <- feedKitty
  } yield s"$greet !!!! $feed"

  greetAndFeed(Cat("Gar", "las"))
}

case class Db(usernames: Map[Int, String], passwords: Map[String, String])

object HackingReaderExercise extends App {
  import cats.syntax.applicative._ // for pure
  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader[Db, Option[String]](_.usernames.get(userId))
  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader[Db, Boolean](_.passwords.get(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      userOpt <- findUsername(userId)
      check <- userOpt match {
        case Some(user) => checkPassword(user, password)
        case None       => false.pure[DbReader]
      }
    } yield check

}
