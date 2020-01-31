package com.github.windbird123.monad

// https://github.com/enshahar/BasicFPinScala/blob/master/Intermediate/Monad.md 에 대한 실습

case class Boxed[T](value: T)
case class Logged[T](value: T, log: List[String])

trait MyOption[+A]
case class MySome[+A](x: A) extends MyOption[A]
case object MyNone extends MyOption[Nothing]

class Lazy[T](value: () => T) {
  def getValue(): T = value()
}

trait MyList[+A]
case class Cons[B](var hd: B, var tl: MyList[B]) extends MyList[B]
case object MyNil extends MyList[Nothing]

object BasicTest extends App {
  def initBoxed(x: Int): Boxed[Int] = Boxed(x)
  def initLogged(x: Int): Logged[Int] = Logged(x, List.empty[String])
  def initMyOption(x: Int): MyOption[Int] = MySome(x)
  def initLazy(x: => Int): Lazy[Int] = new Lazy(() => x)
  def initMyList(x: Int): MyList[Int] = Cons(x, MyNil)

  // functions
  def double(x: Int): Int = x + x
  def sqrt(x: Int): Int = Math.sqrt(x).toInt

  def doubleBoxed(x: Int): Boxed[Int] = Boxed(x + x)
  def sqrtBoxed(x: Int): Boxed[Int] = Boxed(Math.sqrt(x).toInt)

  def doubleLogged(x: Int): Logged[Int] =
    Logged(x + x, List(s"logged ${x + x}"))
  def sqrtLogged(x: Int): Logged[Int] =
    Logged(Math.sqrt(x).toInt, List(s"logged ${Math.sqrt(x).toInt}"))

  def doubleMyOption(x: Int): MyOption[Int] = MySome(x + x)
  def sqrtMyOption(x: Int): MyOption[Int] =
    if (x >= 0) MySome(Math.sqrt(x).toInt) else MyNone

  def doubleLazy(x: => Int): Lazy[Int] = new Lazy(() => x + x)
  def sqrtLazy(x: Int): Lazy[Int] = new Lazy(() => Math.sqrt(x).toInt)

  def doubleMyList(x: Int): MyList[Int] = Cons(x + x, MyNil)
  def sqrtMyList(x: Int): MyList[Int] = Cons(Math.sqrt(x).toInt, MyNil)

  ///////////////////////////////////////////////////////////////////////
  // 조합해 나가기
  ///////////////////////////////////////////////////////////////////////
  def o[T, V, U](f: T => V, g: V => U): T => U = x => g(f(x))

  // double(x:Int) 와 sqrt(x:Int) 조합하기
  val doubleThenSqrt = o(double, sqrt)
  doubleThenSqrt(8) // 4

  // curry 형태로 만들어 보기
  def o2[T, V, U](f: T => V) = (g: V => U) => (x: T) => g(f(x))
  o2(double)(sqrt)
}
