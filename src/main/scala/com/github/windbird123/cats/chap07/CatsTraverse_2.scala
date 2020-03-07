package com.github.windbird123.cats.chap07


object CatsTraverseTest {
  import cats.Applicative
  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.instances.future._ // for Applicative
  import scala.concurrent.Future
  import cats.syntax.applicative._ // for pure
  import cats.syntax.apply._ // for mapN

  def listTraverse[F[_]: Applicative, A, B](
    list: List[A]
  )(f: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (acc, item) =>
      (acc, f(item)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  def main(args: Array[String]): Unit = {
    import cats.instances.vector._ // for Applicative
    listSequence(List(Vector(1,2), Vector(3,4)))  // Vector(List(1, 3), List(1, 4), List(2, 3), List(2, 4))

    import cats.instances.list._
    import cats.syntax.traverse._
    List(Future(1), Future(2), Future(3)).sequence  // Future(List(1,2,3))


  }
}
