package examples

import cats.Semigroup
import cats.data.Validated
import cats.syntax.apply._ // for mapN

sealed trait Check[E, A] {
  def and(that: Check[E, A]): Check[E, A] =
    And(this, that)
  def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = this match {
    case Pure(func) =>
      func(a)
    case And(left, right) =>
      (left(a), right(a)).mapN((_, _) => a)
  }
}
final case class And[E, A](left: Check[E, A], right: Check[E, A])
    extends Check[E, A]
final case class Pure[E, A](func: A => Validated[E, A]) extends Check[E, A]
