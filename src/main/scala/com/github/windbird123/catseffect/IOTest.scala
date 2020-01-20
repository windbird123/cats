package com.github.windbird123.catseffect

import java.util.Date

import cats.effect.IO

object IOTest extends App {
  val io1 = IO {
    Thread.sleep(3000L)
    println("io1")
  }
  val io2 = IO {
    Thread.sleep(3000L)
    println("io2")
  }

  val io = for {
    x <- io1
    y <- io2
  } yield ()

  println(new Date())
  io.unsafeRunSync()
  println(new Date())

  Thread.sleep(1000)
}

object IOPureTest extends App {
  // IO.pure cannot suspend side effects
  IO.pure(25).flatMap(n => IO(println(s"number is :$n")))

  // Do not this
  IO.pure(println("THIS IS WRONG!"))
}

object IOApply extends App {
  // IO.apply is equivalent of Sync[IO].delay
  def putStrln(value: String) = IO(println(value))
  val readLn = IO(scala.io.StdIn.readLine())

  val io = for {
    _ <- putStrln("your name?")
    n <- readLn
    _ <- putStrln(s"Hello, $n!")
  } yield ()

  io.unsafeRunSync()
}

object ParTest extends App {
  import cats.data._
  import cats.effect.IO._
  import cats.effect.{ContextShift, IO}
  import cats.syntax.all._

  import scala.concurrent.ExecutionContext
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val io1 = IO {
    Thread.sleep(3000L)
    println("io1")
  }

  val io2 = IO {
    Thread.sleep(3000L)
    println("io2")
  }

  val io3 = IO {
    Thread.sleep(3000L)
    println("io3")
  }

  val ioList = NonEmptyList.of(io1, io2, io3)
  println(new Date())
  val ios = ioList.parSequence
  ios.unsafeRunSync()
  println(new Date())
}

object Traverse extends App {
  import cats.instances.future._
  import cats.instances.list._
  import cats.syntax.traverse._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._

  val list = List("1", "2", "3", "4", "5")
  val out = list.traverse(
    x =>
      Future {
        Thread.sleep(1000L)
        println(x)
        x
    }
  )

  println("---- start")
  println(out.value)
  println("---- end")
  Thread.sleep(3000L)
}
