package com.github.windbird123.fs2

import cats.effect.{Async, ContextShift, IO, Timer}
import fs2.Chunk

import scala.concurrent.ExecutionContext

object CreateStreamTest extends App {
  // scala stream
  val stream = Stream(1, 2, 3, 4)

  // fs2 stream
//  fs2.Stream.eval(IO(2)).compile.toList.unsafeRunSync

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
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  fs2.Stream
    .emits[IO, Char]('A' to 'E')
    .map(
      letter => fs2.Stream.emits[IO, Int](1 to 3).map(index => s"$letter$index")
    )
    .parJoin(5)

  // Batching
  fs2.Stream.emits(1 to 100).chunkN(10).map(println).compile.drain

  val s = fs2.Stream(1, 2) ++ fs2.Stream(3) ++ fs2.Stream(4, 5, 6)
  val chunks = s.chunks.toList // List(Chunk(1, 2), Chunk(3), Chunk(4, 5, 6))

  import scala.concurrent.duration._
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  fs2.Stream
    .awakeEvery[IO](10.millis)
    .groupWithin(100, 100.millis)
    .evalTap(
      chuck => IO(println(s"Processing batch of ${chuck.size} elements"))
    )
    .compile
    .drain

  // Asynchronous computation
  def writeToDatabase[F[_]: Async](chunk: Chunk[Int]) : F[Unit] = Async[F].async { callback =>
    println(s"Writing batch of $chunk to database by ${Thread.currentThread().getName}")
//    callback(Right())
  }

  fs2.Stream.emits(1 to 10000)
      .chunkN(10)
      .covary[IO]
      .parEvalMap(10)(writeToDatabase[IO])
      .compile
      .drain
  // parEvalMap preserves the stream ordering
  // note: mapAsyncUnordered
}
