package com.github.windbird123.fs2

import cats.effect.IO

import scala.concurrent.ExecutionContext

object CreateStreamTest extends App {
  // scala stream
  val stream = Stream(1, 2, 3, 4)

  // fs2 stream
  fs2.Stream.eval(IO(2)).compile.toList.unsafeRunSync

  fs2.Stream
    .emits('A' to 'E')
    .map(letter => (1 to 3).map(index => s"$letter$index"))
    .flatMap(fs2.Stream.emits)
    .compile
    .toList

  // or
  fs2.Stream
    .emits[fs2.Pure, Char]('A' to 'E')
    .map(letter => fs2.Stream.emits(1 to 3).map(index => s"$letter$index"))
    .flatten
    .compile
    .toList

  // process parallel
  implicit val cs = IO.contextShift(ExecutionContext.global)
  fs2.Stream
    .emits[IO, Char]('A' to 'E')
    .map(
      letter => fs2.Stream.emits[IO, Int](1 to 3).map(index => s"$letter$index")
    )
    .parJoin(5)


  // Batching
  fs2.Stream.emits(1 to 100).chunkN(10).map(println).compile.drain

}
