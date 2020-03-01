package com.github.windbird123.cats.chap04

object CatsMonadTest {
  def main(args: Array[String]): Unit = {
    // cats 는 Future Monad 도 제공한다.
    import cats.Monad
    import cats.instances.future._

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent._
    import scala.concurrent.duration._
    val fm = Monad[Future]

    val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
    Await.result(future, 1.second) // 3

    // Monad syntax
    import cats.instances.option._ // for Monad
    import cats.instances.list._ // for Monad
    import cats.syntax.applicative._ // for pure

    1.pure[Option] // Some(1)
    1.pure[List] //  List(1)
  }

}

class CatsMonad_2 {}
