package examples.monid

import cats._
import cats.data._
import cats.implicits._
import examples._

object FoldableApp extends App {
//Foldable is for things that can be folded up!
  Foldable[List].foldLeft(List(1, 2, 3), 1)(_ * _).print()

  List(1, 2, 3).foldLeft(1)((b, a) => a * b).print()
  List(1, 2, 3).product.print()

  Foldable[List].fold(List(1, 2, 3))(Monoid[Int]).print()
  List(1, 2, 3).foldMap(identity)(Monoid[Int]).print()
  List(1, 2, 3).foldMap(_ + 1)(Monoid[Int]).print()
}
