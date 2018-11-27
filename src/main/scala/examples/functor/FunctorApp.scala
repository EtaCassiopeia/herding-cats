package examples.functor

import cats._
import cats.data._
import cats.implicits._
import cats.syntax._
import examples._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object FunctorApp extends App {
  val list = List("one", "two")

  val liftedList = Functor[List].lift[String, String](s => s"lifted $s")
  liftedList(list).print()

  def getOfficialAccountInfo(s: String): String =
    s"Official Account info for $s"
  Functor[List]
    .fproduct[String, String](list)(getOfficialAccountInfo)
    .print()

  list.map(s => (s, getOfficialAccountInfo(s))).print()

  ((Right(1): Either[String, Int]) map { _ * 10 })
    .print()

  (Left("Pure Error"): Either[String, Int])
    .leftMap(s => s"Bilaaakh instead of $s")
    .print()

  val intList = List(1, 2, 3)
  val applicativeList: List[Int => Int] =
    Functor[List].map(intList)(a => (b: Int) => a * b)
  Functor[List].map(applicativeList)(_(2)).print()

  case class Person(name: String, lastName: String)

  Applicative[Option]
    .map2(Some("mohsen"), Some("zainalpour"))(Person.apply)
    .print()

  //Functor composition
  val result: Future[List[String]] = Future.successful(list)
  Await
    .result(result.map(_.map(s => s"new $s")), 10 seconds)
    .print() //nested maps

  val futureListF = Functor[Future].compose(Functor[List]) //F[A], F[B] => F[A[B]]
  Await.result(futureListF.map(result)(s => s"new $s"), 10 seconds).print()

}
