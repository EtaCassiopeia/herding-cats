package examples.applicative
import scalaz._
import Scalaz._

object FunctionsAsApplicativeApp extends App {
  //Functions are instance of Functor
  val f = (_: Int) + 2
  val g = (_: Int) * 5

  println((g map f)(5))

  //Functions are applicative Functors.
  val h = ({ (_: Int) * 2 } |@| { (_: Int) + 10 }) { _ + _ } //|@| ApplicativeBuilder
  println(h(3))

  //Not only is the function type (->) r a functor and an applicative functor, but it’s also a monad.
  val addStuff: Int => Int = for {
    a <- (_: Int) * 2
    b <- (_: Int) + 10
  } yield a + b

  println(addStuff(3))

  //Reader Monad: Both (*2) and (+10) get applied to the number 3 in this case. return (a+b) does as well, but it ignores it and always presents a+b as the result. For this reason, the function monad is also called the reader monad. All the functions read from a common source.
}
