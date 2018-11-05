package examples.tagged_type

import scalaz._
import Scalaz._

object TaggedTypeClassApp extends App {

  val result = 10 |+| Monoid[Int].zero

  val result2
    : @@[Int, Tags.Multiplication] = Tags.Multiplication(10) |+| Monoid[
    Int @@ Tags.Multiplication].zero //here SemiGroup means multiplication

  println(result)
  println(Monoid[Int].zero)

  println(result2)
  println(Monoid[Int @@ Tags.Multiplication].zero)

  val xs = List(1, 2, 3).map(Tags.Multiplication(_))

  trait Disjunction

  object Disjunction {
    def apply[A](a: A): A @@ Disjunction = Tag[A, Disjunction](a)
  }

  implicit val booleanDisjunctionMonoid: Monoid[Boolean @@ Disjunction] = {
    new Monoid[Boolean @@ Disjunction] {
      override def zero: Boolean @@ Disjunction = Disjunction(true)

      override def append(
          f1: Boolean @@ Disjunction,
          f2: => Boolean @@ Disjunction): Boolean @@ Disjunction =
        Disjunction(Tag.unwrap(f1) || Tag.unwrap(f2))
    }
  }

  val result3 = Disjunction(true) |+| Disjunction(false)
  println(result3)

  trait Conjunction
  object Conjunction {
    def apply[A](a: A): A @@ Conjunction = Tag[A, Conjunction](a)
  }

  implicit val booleanConjunctionMonoid: Monoid[Boolean @@ Conjunction] = {
    new Monoid[Boolean @@ Conjunction] {
      override def zero: @@[Boolean, Conjunction] = Conjunction(true)

      override def append(
          f1: @@[Boolean, Conjunction],
          f2: => @@[Boolean, Conjunction]): @@[Boolean, Conjunction] =
        Conjunction(Tag.unwrap(f1) && Tag.unwrap(f2))
    }
  }

  val result4 = Conjunction(true) |+| Conjunction(false)
  println(result4)

}
