package com.github.windbird123.cats.chap4

object CatsEvalTest extends App {
  import cats.Eval

  val now = Eval.now(3) // val - eager and memoized
  val later = Eval.later(4) // lazy val - lazy and memoized
  val always = Eval.always(5) // def - lazy

  val greeting = Eval
    .always {
      println("Step 1")
      "Hello"
    }
    .map { str =>
      println("Step 2")
      s"$str world"
    }

  val out = greeting.value
  println(out)

  println("----------------------")

  val ans = for {
    a <- Eval.now {
      println("Calc A")
      40
    }

    b <- Eval.always {
      println("Calc B")
      2
    }
  } yield {
    println("Adding A and B")
    a + b
  }

  println("Before -------------")
  ans.value
  ans.value
}

object EvalMemoizeTest extends App {
  import cats.Eval

  val saying = Eval
    .always {
      println("Step 1")
      "The cat"
    }
    .map { str =>
      println("Step 2")
      s"$str sat on"
    }
    .memoize
    .map { str =>
      println("Step 3")
      s"$str the mat"
    }

  println("Before-----------")
  saying.value
  saying.value
}

object EvalTrampolining extends App {
  import cats.Eval

  // factorial(50000) => stack overflow
  def factorial(n: BigInt): BigInt = if (n == 1) 1 else n * factorial(n - 1)

  def safeFactorial(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(1)
    } else {
      Eval.defer {
        safeFactorial(n - 1).map(_ * n)
      }
    }

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = {
    as match {
      case Nil => acc
      case head :: tail =>
        foldRight(tail, fn(head, acc))(fn)
    }
  }

  def safeFoldRight[A, B](as: List[A], acc: Eval[B])(
    fn: (A, Eval[B]) => Eval[B]
  ): Eval[B] = {
    as match {
      case Nil => acc
      case head :: tail =>
        Eval.defer {
          foldRight(tail, fn(head, acc))(fn)
        }
    }
  }

  val out = safeFoldRight((1L to 100000L).toList, Eval.now(0L))(
    (x, acc) => acc.map(_ + x)
  )
  println(out.value)

  def safeFoldRight2[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    safeFoldRight(as, Eval.now(acc)) { (a, b) =>
      b.map(fn(a, _))
    }.value

  safeFoldRight2((1 to 100000).toList, 0L)(_ + _)
}
