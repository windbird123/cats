package com.github.windbird123.cats.chap4

import cats.data.State

object CatsStateTest extends App {
  val step1 = State[Int, String] { state =>
    val changedState = state + 1
    (changedState, s"Result step1: $changedState")
  }

  val step2 = State[Int, String] { state =>
    val changedState = state * 2
    (changedState, s"Result state2: $changedState")
  }

  val both = for {
    resultOfStep1 <- step1
    resultOfStep2 <- step2
  } yield (resultOfStep1, resultOfStep2)

  val (state, result) = both.run(20).value
  println(state) // 42
  println(result) // (Result step1: 21,Result state2: 42)

  val program: State[Int, (Int, Int, Int)] = for {
    a <- State.get[Int] // extracts the state as the result, state=1, result=1
    _ <- State.set[Int](a + 1) // only update state, state=2, result=1
    b <- State.get[Int] // state=2, result=2
    _ <- State.modify[Int](_ + 1) // only transform state, state=3, result=3
    c <- State.inspect[Int, Int](_ * 1000) // state=3, result=3000
  } yield (a, b, c)

  val (state2, result2) = program.run(1).value
  // state2: Int = 3
  // result: (Int, Int, Int) = (1,2,3000)

  // ---------------------------------------------
  // 위의 프로그램은 아래와 동일하다
  // ---------------------------------------------
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

  val out = s.run(1).value
  println(out)
}

object PostOrderCalcExercise extends App {
  import cats.data.State
  type CalcState[A] = State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] = sym match {
    case "+" =>
      State[List[Int], Int](intList => (List.empty[Int], intList.sum))
    case _ =>
      State[List[Int], Int](intList => (sym.toInt :: intList, sym.toInt))
  }

  val prog = for {
    _ <- evalOne("1")
    _ <- evalOne("2")
    ans <- evalOne("+")
  } yield ans

  val out = prog.run(Nil).value
  println(out)
}
