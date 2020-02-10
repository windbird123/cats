package com.github.windbird123.cats.chap6

object SemigroupalTest extends App {
  import cats.Semigroupal
  import cats.instances.list._
  import cats.instances.option._

  Semigroupal[Option].product(Some(123), Some("abc")) // Some((123, "abc"))

  import cats.syntax.apply._
  (Option(123), Option("abc")).tupled

  val lt = (List(1, 2), List(3, 4)).tupled
  println(lt) // List((1,3), (1,4), (2,3), (2,4))

  import cats.Semigroupal
  import cats.instances.future._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._
  import scala.concurrent.duration._

  val futurePair = Semigroupal[Future].product(Future {
    Thread.sleep(2000L); println("hi"); 10
  }, Future { Thread.sleep(1000L); println("there"); 20 })

  val r = Await.result(futurePair, 5.second)
  println(r)

}

object ExerciseProductMonad extends App {
  import cats.Monad

  def product[M[_]: Monad, A, B](x: M[A], y: M[B]): M[(A, B)] =
    Monad[M].flatMap(x)(a => Monad[M].map(y)(b => (a, b)))
}
