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
