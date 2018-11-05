package examples.apply

import cats._
import cats.implicits._
import cats.syntax._
import examples._

object ApplyApp extends App {

  val f1: Int => Int => Int = { (_: Int) * (_: Int) }.curried
  val f2: Int => Int => Int = (a: Int) => (b: Int) => a * b
  val f3: Int => Int => Int = ((a: Int, b: Int) => a * b).curried
  val f4: (Int, Int) => Int = (_: Int) * (_: Int)

  val mappedList: List[Int => Int] = Functor[List]
    .map(List(1, 2, 3)) {
      { (_: Int) * (_: Int) }.curried
    }

  Functor[List].map(mappedList)(_(2)).print()
  Functor[List].map(mappedList)(_.apply(2)).print()
  Functor[List]
    .map(mappedList) { f =>
      f(2)
    }
    .print()

  /*
  Applicatives:
    1- Cartesian
    2- Apply
    3- Applicative
   */
  val mappedOption: Option[Int => Int] = Some((_: Int) + 2)
  Apply[Option].ap(mappedOption)(Some(2)).print()

  val mappedOptionWithTwoParameters = Some((a: Int, b: Int) => a * b)
  Apply[Option].ap2(mappedOptionWithTwoParameters)(Some(1), Some(2)).print()

  case class Person(name: String, lastName: String)
  Apply[Option]
    .ap2(Some(Person.apply _))(
      Some("Mohsen"),
      Some("Zainalpour")
    )
    .print()

  (for {
    name <- Some("Mohsen")
    lastName <- Some("Zainalpour")
  } yield Person(name, lastName)).print()

  Some("Mohsen")
    .flatMap(name => {
      Some("Zainalpour").map(lastName => Person(name, lastName))
    })
    .print()

  Applicative[Option]
    .map2(Some("Mohsen"), Some("Zainalpour"))(Person.apply)
    .print()

  ("Mohsen".some, "Zainalpour".some)
    .mapN(Person.apply)
    .print()

  Applicative[Option].map2(Some(1), Some(2))(_ + _).print()
  (1.some, 2.some).mapN(_ + _).print()

  (List("one", "two", "three"), List("Apple", "Orange")).mapN(_ + _).print()

  //special cases of Apply[F].map2
  (1.some <* 2.some).print()
  (none[Int] <* 2.some).print()
  (1.some *> 2.some).print()
  (none[Int] *> 2.some).print()

  ((3.some, List(4).some) mapN { _ :: _ }).print()

  Apply[Option].map2(3.some, List(4).some) {
    _ :: _
  }.print()

  Apply[Option].ap2({{ (_: Int) :: (_: List[Int]) }.some })(3.some, List(4).some).print()

  Apply[Option].tuple2(1.some, 2.some).print()
}
