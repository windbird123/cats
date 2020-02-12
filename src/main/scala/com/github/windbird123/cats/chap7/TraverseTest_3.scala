package com.github.windbird123.cats.chap7

object TraverseTest extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._
  import scala.concurrent.duration._

  val hostnames =
    List("alpha.example.com", "beta.example.com", "gamma.demo.com")

  def getUptime(hostname: String): Future[Int] = Future(hostname.length * 60)

  val allUptimesV1: Future[List[Int]] =
    hostnames.foldLeft(Future(List.empty[Int])) { (acc, host) =>
      for {
        a <- acc
        uptime <- getUptime(host)
      } yield a :+ uptime
    }

  Await.result(allUptimesV1, 1.second)

  val allUptimesV2: Future[List[Int]] = Future.traverse(hostnames)(getUptime)

  // traverse for List
  def traverse[A, B](values: List[A])(func: A => Future[B]): Future[List[B]] =
    values.foldLeft(Future(List.empty[B])) { (acc, host) =>
      val item = func(host)
      for {
        a <- acc
        uptime <- item
      } yield a :+ uptime
    }
}

object OptionTraverseTest extends App {
  import cats.syntax.traverse._
  import cats.syntax.option._
  import cats.instances.option._
  import cats.instances.list._

  val l = List(1, 2, 3)
  val out = l.traverse { x: Int =>
    if (x % 2 == 0) none[Int] else x.some
  }
  println(out)
}
