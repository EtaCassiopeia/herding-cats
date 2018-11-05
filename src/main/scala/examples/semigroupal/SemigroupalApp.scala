package examples.semigroupal

import cats._
import cats.data._
import cats.implicits._
import cats.syntax._

import examples._

object SemigroupalApp extends App {
  val e1 = Right(1).toOption
  val e2 = Right(2).toOption

  Semigroupal[Option].product(e1, e2).print()

  (for {
    o1 <- e1
    o2 <- e2
  } yield (o1, o2)).print()

  Applicative[Option].map2(e1,e2){
    (o1,o2) => (o1,o2)
  }.print()
}
