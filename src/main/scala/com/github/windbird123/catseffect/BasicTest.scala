package com.github.windbird123.catseffect

import java.net.Socket
import java.util.concurrent.CompletableFuture

import cats.effect.IO

// https://beyondthelines.net/programming/cats-effect-an-overview/
object NotRT extends App {
  def k = {
    print("n")
    3
  }

  println(s"$k + $k") // nn3 + 3   ==> NOT RT
}

object Principle extends App {
  val io = IO {
    print("n")
    3
  }

//  io.unsafeRunSync()
//  io.unsafeToFuture()
}

object Conditional extends App {
  val isWeekday = true

  // cats-effect 상위 버전에서는 아래가 되는 듯
//  for {
//   _ <- IO(println("Working")).whenA(isWeekday)
//   _ <- IO(println("Offwork")).unlessA(isWeekday)
//  }  yield ()
}

object AsynchronousIOTest extends App {
  // convert Java Future to IO
  def convert[A](f: => CompletableFuture[A]): IO[A] = IO.async { callback =>
    f.whenComplete { (res: A, error: Throwable) =>
      if (error == null) callback(Right(res))
      else callback(Left(error))
    }
  }
}

object BracketTest extends App {
  IO(new Socket("hostname", 12345))
    .bracket(socket => IO { println("hi") })(socket => IO { socket.close() })

  // bracketCase 도 참고
}

object ResourceTest extends App {
  import cats.implicits._
  def acquire(s: String) = IO(println(s"Acquire $s")) *> IO.pure(s)

  def release(s: String) = IO(println(s"Release $s"))

  // Resource is composable so that you can acquire all your resources at once.
//  val resources =  for {
//    a<-Resource.make(acquire("A"))(release("A"))
//    b<-Resource.make(acquire("B"))(release("B"))
//  } yield (a, b)
//
//  resources.use {
//    case (a, b) =>
//  }

  lazy val a =  {
    println("hi")
    "a".some
  }

  lazy val b =  { none[String] }

  lazy val c = "c".some

  val d = c <* a

  println(d)
}
