package examples.trampoline

import scalaz._
import Scalaz._
import scalaz.Free.Trampoline
import Free._

object TrampolineApp extends App {
  //Trampolining and stack safety

  //problem
  /**def even[A](lst: List[A]): Boolean = {
    lst match {
      case Nil     => true
      case x :: xs => odd(xs)
    }
  }

  def odd[A](lst: List[A]): Boolean = {
    lst match {
      case Nil     => false
      case x :: xs => even(xs)
    }
  }

  even((0 to 1000000).toList) // blows the stack

    every method call adds an entry to stack and it blows the stack.
    Scala is able to optimize the recursive calls only if it is a self-recursive call (tail recursive)

    Using Trampoline(Free Monad ( type Trampoline[A] = Free[Function0, A])) we can describe the calls instead of making real calls and we can make it tail recursive
    This deferring - in scalaz - is called suspension because you are suspending the execution of that function and you will resume it later
    **/
  //Called Trampoline because every time we .bind on the stack, we bounce back to the heap.
  def even[A](ns: List[A]): Trampoline[Boolean] =
    ns match {
      case Nil     => Trampoline.done(true) // or return_(true)
      case x :: xs => Trampoline.suspend(odd(xs)) //or suspend(odd(xs))
    }
  def odd[A](ns: List[A]): Trampoline[Boolean] =
    ns match {
      case Nil     => return_(false)
      case x :: xs => suspend(even(xs))
    }

  println(even(0 |-> 1000000).run)
}
