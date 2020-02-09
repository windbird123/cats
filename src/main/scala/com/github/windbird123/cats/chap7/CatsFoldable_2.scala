package com.github.windbird123.cats.chap7

class CatsFoldableTest extends App {
  import cats.Foldable
  import cats.instances.list._ // for Foldable

  val ints = List(1, 2, 3)
  Foldable[List].foldLeft(ints, 0)(_ + _)

  import cats.instances.stream._
  import cats.Eval

  // stack safe
  def bigData = (1 to 100000).toStream
  val eval: Eval[Long] = Foldable[Stream].foldRight(bigData, Eval.now(0L)) {
    (num, evalAcc) =>
      evalAcc.map(_ + num)
  }
  eval.value
}
