package com.github.windbird123.cats.chap04

import cats.data.State

object CatsStateTest {
  def main(args: Array[String]): Unit = {
    val step1 = State[Int, String] { num =>
      val state1 = num + 1
      (state1, s"step1: $state1")
    }

    val step2 = State[Int, String] { num =>
      val state2 = num * 2
      (state2, s"step2: $state2")
    }

    val both = for {
      a <- step1
      b <- step2
    } yield a + " " + b

    both.run(20).value // (42,step1: 21 step2: 42)
  }
}

object ListStateTest {
  def main(args: Array[String]): Unit = {
    val s1 =
      State[List[String], Int](list => (list.map(_ + "A"), list.length))

    val s2 = State[List[String], Int](
      list => (list.map(_ + "B"), list.map(_.length).max)
    )

    val s = for {
      x <- s1
      y <- s2
    } yield x + y

    s.run(List("a", "ab", "abc"))
      .value // state=List(aAB, abAB, abcAB), result=7
  }
}

object CompositionTest {
  def main(args: Array[String]): Unit = {
    val s1 = State[Int, Int](s => (s, s))
    val s2 = State[Int, Int](s => (s + 1, 0))
    val s3 = State[Int, Int](s => (s, s))
    val s4 = State[Int, Int](s => (s + 1, 0))
    val s5 = State[Int, Int](s => (s, s * 1000))

    val s = for {
      a <- s1
      _ <- s2
      b <- s3
      _ <- s4
      c <- s5
    } yield (a, b, c)

    s.run(1).value // state=3, result=(1, 2, 3000)
    // 1, 1
    // 2
    // 2, 2
    // 3
    // 3, 3000
  }
}

object PostOrderState {
  def evalOne(s: String): State[List[String], Int] = s match {
    case "+" =>
      State[List[String], Int] {
        case b :: a :: tail =>
          val ans = a.toInt + b.toInt
          val state = ans.toString :: tail
          (state, ans)

        case _ => sys.error("fail")
      }

    case "*" =>
      State[List[String], Int] {
        case b :: a :: tail =>
          val ans = a.toInt * b.toInt
          val state = ans.toString :: tail
          (state, ans)

        case _ => sys.error("fail")
      }

    case x =>
      State[List[String], Int] { list =>
        (x :: list, x.toInt)
      }
  }

  // evalOne 을 아래 처럼 고도화 할 수 있다.  ------------------------
  def operator(f: (Int, Int) => Int): State[List[String], Int] =
    State[List[String], Int] {
      case b :: a :: tail =>
        val ans = f(a.toInt, b.toInt)
        val state = ans.toString :: tail
        (state, ans)

      case _ => sys.error("fail")
    }

  def evalOneV2(s: String): State[List[String], Int] = s match {
    case "+" => operator(_ + _)
    case "*" => operator(_ * _)
    case x =>
      State[List[String], Int] { list =>
        (x :: list, 0)
      }
  }
  // ----------------------------------------------------------------

  def evalAll(input: List[String]): State[List[String], Int] = {
    val initVal = State((_: List[String]) => (List.empty[String], 0))
    input.foldLeft(initVal) {
      case (acc, x) => acc.flatMap(_ => evalOne(x))
    }
  }
}
