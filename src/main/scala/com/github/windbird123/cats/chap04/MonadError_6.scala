package com.github.windbird123.cats.chap04

object TryMonadErrorTest {
  def main(args: Array[String]): Unit = {
    import cats.MonadError
    import cats.instances.future._
    import cats.instances.try_._
    import cats.syntax.applicativeError._

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scala.util.Try

    def parseUser[F[_]](
      s: String
    )(implicit me: MonadError[F, Throwable]): F[Int] = s match {
      case "1" => me.pure(1)
      case "2" => me.raiseError(new Exception("2"))
      case "3" =>
        // fromTry 보다는 아래처럼 catchNonFatal 또는 catchNonFatalEval 을 사용하자
        me.fromTry {
          Try {
            1 + 2
          }
        }

      case _ =>
        me.catchNonFatal[Int] {
            throw new Exception("4")
          }
          .recoverWith {
            case ex => me.pure(4)
          }
    }

    val user = parseUser[Try]("3")
    println(user)

    val user2 = parseUser[Future]("4")
    println(user2)
  }
}
