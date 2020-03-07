package com.github.windbird123.cats.chap08

import cats.Id

import scala.concurrent.Future

trait UptimeClient[F[_]] {
  def getUptime(hostname: String): F[Int]
}

class RealClient extends UptimeClient[Future] {
  def getUptime(hostname: String): Future[Int] = ???
}

class TestClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
  def getUptime(hostname: String): Id[Int] = hosts.getOrElse(hostname, 0)
}

object UptimeServiceTest {

  def main(args: Array[String]): Unit = {
    val hosts    = Map("host1" -> 10, "host2" -> 6)
    val client = new TestClient(hosts)

    val actual = client.getUptime("host1")
    assert(actual == 10)
  }
}
