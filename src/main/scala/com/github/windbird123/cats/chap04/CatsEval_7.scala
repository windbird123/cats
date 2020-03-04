package com.github.windbird123.cats.chap04

import cats.Eval

object AlwaysTest {
  def main(args: Array[String]): Unit = {
    val greeting = Eval
      .always {
        println("Step 1")
        "Hello"
      }
      .map { str =>
        println("Step 2")
        s"$str world"
      }

    println("START---------------------")
    println(greeting.value)

    //START---------------------
    //Step 1
    //Step 2
    //Hello world
  }
}

object NowTest {
  def main(args: Array[String]): Unit = {
    val greeting = Eval
      .now {
        println("Step 1")
        "Hello"
      }
      .map { str => // lazy!!!
        println("Step 2")
        s"$str world"
      }

    println("START---------------------")
    println(greeting.value)

    //Step 1
    //START---------------------
    //Step 2
    //Hello world
  }
}

object TestCase2 {
  def main(args: Array[String]): Unit = {
    val ans = for {
      a <- Eval.now {
        println("A")
        40
      }
      b <- Eval.always {
        println("B")
        2
      }
    } yield {
      println("AB")
      a + b
    }

    println("START---------------------")
    println(ans.value)
    println(ans.value)

    //A
    //START---------------------
    //B
    //AB
    //42
    //B
    //AB
    //42
  }
}

object TestCase3 {
  def main(args: Array[String]): Unit = {

    val saying = Eval
      .always {
        println("A")
        "A"
      }
      .map { str =>
        println("B")
        s"$str B"
      }
      .memoize
      .map { str =>
        println("C")
        s"$str C"
      }

    println("START---------------------")
    println(saying.value)
    println(saying.value)

    //START---------------------
    //A
    //B
    //C
    //A B C
    //C
    //A B C
  }
}

object TrampoliningEval {
  def factorial(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(n)
    } else {
      Eval.defer { factorial(n - 1).map(_ * n) }
    }

  def main(args: Array[String]): Unit = {
    factorial(10).value
  }
}

object SafeFolding {
  // NOT stack safe
  def foldRight[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
    as match {
      case head :: tail => f(head, foldRight(tail, acc)(f))
      case Nil          => acc
    }

  // stack safe
  def foldRightSafe[A, B](as: List[A],
                          acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail => Eval.defer { f(head, foldRight(tail, acc)(f)) }
      case Nil          => acc
    }

  def foldRightFinal[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
    foldRightSafe(as, Eval.now(acc))((a: A, eB: Eval[B]) => eB.map(f(a, _))).value
}
