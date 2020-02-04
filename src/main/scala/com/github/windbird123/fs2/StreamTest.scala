package com.github.windbird123.fs2

import cats.effect.IO

object CreateStreamTest extends App {
  // scala stream
  val stream = Stream(1, 2, 3, 4)

  // fs2 stream
  fs2.Stream.eval(IO(2))
      .compile
      .toList
      .unsafeRunSync

}
