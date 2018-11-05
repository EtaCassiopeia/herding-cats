package examples.semigroup

import cats._
import cats.implicits._
import cats.syntax._
import examples._

object SemigroupApp extends App {
  val result1: Int = Semigroup[Int].combine(2, 3)
  result1.print()

  val result2: Int = 2 |+| 3
  val result3: List[Int] = List(1, 2, 3) |+| List(4, 5, 6)
  result3.print()

  val result4: List[Int] = List(1, 2, 3) ++ List(4, 5, 6)
  result4.print()

  def doSomething[A: Semigroup](a: A, b: A): A = a |+| b

  doSomething(2, 3)(Semigroup[Int]).print()

  case class ParseError(message: String)

  implicit val errorSemigroup: Semigroup[ParseError] {
    def combine(x: ParseError, y: ParseError): ParseError
  } = new Semigroup[ParseError] {
    override def combine(x: ParseError, y: ParseError): ParseError =
      ParseError(x.message |+| ", " |+| y.message)
  }

  (ParseError("Element 'name' is not found") |+| ParseError(
    "Age should be an Int"))
    .print()
}
