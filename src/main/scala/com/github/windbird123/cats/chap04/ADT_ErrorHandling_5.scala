package com.github.windbird123.cats.chap04

case class User(name: String, password: String)

sealed trait LoginError
final case class UserNotFound(username: String) extends LoginError
final case class PasswordIncorrect(username: String) extends LoginError
case object UnexpectedError extends LoginError

object ErrorHandlingTest {
  type LoginResult = Either[LoginError, User]

  def handleError(error: LoginError): Unit = error match {
    case UserNotFound(user)      => println(s"User not found: $user")
    case PasswordIncorrect(user) => println(s"Password incorrect: $user")
    case UnexpectedError         => println("Unexpected error")
  }

  import cats.syntax.either._
  val result1: LoginResult = User("dave", "pass").asRight
  val result2: LoginResult = UserNotFound("dave").asLeft

  result1.fold(handleError, println) // User(dave,passw0rd)
  result2.fold(handleError, println) // User not found: dave
}
