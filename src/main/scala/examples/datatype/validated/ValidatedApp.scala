package examples.datatype.validated

import cats.data.{NonEmptyList => NEL}
import cats._
import cats.data._
import cats.implicits._
import cats.syntax._
import Validated.{valid, invalid}
import examples._

object ValidatedApp extends App {
  //Validated is another datatype in Cats that we can use in place of Either
  //What’s different about Validation is that it is does not form a monad, but forms an applicative functor.
  //Instead of chaining the result from first event to the next, Validated validates all events:

  val result =
    (valid[NEL[String], String]("event 1 ok") |@|
      invalid[NEL[String], String](NEL.of("event 2 failed!")) |@|
      invalid[NEL[String], String](NEL.of("event 3 failed!"))) map { _ + _ + _ }

  result.print()
}
