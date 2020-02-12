package com.github.windbird123.cats.chap8

import cats.{Applicative, Id}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UptimeClient {
  def getUptime(hostname: String): Future[Int]
}

import cats.instances.future._
import cats.instances.list._
import cats.syntax.traverse._ // for traverse

class UptimeService(client: UptimeClient) {
  def getTotalUptime(hostnames: List[String]): Future[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
}

class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient {
  def getUptime(hostname: String): Future[Int] =
    Future.successful(hosts.getOrElse(hostname, 0))
}

object AsyncTest extends App {
  def testTotalUptime() = {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)

    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
//     assert(actual == expected) // actual 과 expected 는 type 이 달라 비교 불가...
    assert(actual == Future.successful(expected))
  }
}

trait UptimeClientV2[F[_]] {
  def getUptime(hostname: String): F[Int]
}

trait RealUptimeClientV2 extends UptimeClientV2[Future] {
  override def getUptime(hostname: String): Future[Int]
}

trait TestUptimeClientV2 extends UptimeClientV2[Id] {
  override def getUptime(hostname: String): Int
}

import cats.syntax.functor._
class UptimeServiceV2[F[_]: Applicative](client: UptimeClientV2[F]) {
  def getTotalUptime(hostnames: List[String]): F[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
}
