package examples.monad_transformer

import examples.monad_transformer.?.Result
import scalaz.{Applicative, _}
import Scalaz._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

object ? {
  type Result[A] = OptionT[Future, A]

  def <~[A](v: Future[Option[A]]): Result[A] = OptionT(v)
  def <~[A](v: Option[A]): Result[A] = OptionT(Future.successful(v))
  def <~[A](v: A): Result[A] =
    OptionT(Future.successful(v.some))
}

object OptionTApp extends App {

  //Monad transformers allow us to stack monads. Say we have a monad, like Option, and we want to wrap it in another monad, like \/, in a convenient way
  case class Country(code: Option[String])

  case class Address(addressId: String, country: Option[Country])

  case class Person(name: String, address: Option[Address])

//  def findPerson(id: String): Option[Person] = ???
  def findPerson(id: String): Future[Option[Person]] = ???
  def findCountry(addressId: String): Future[Option[Country]] = ???

  def getCountryCode(personId: String): Future[Option[String]] = {

    val result: OptionT[Future, String] = for {
      person <- OptionT(findPerson(personId))
      address <- OptionT(Future.successful(person.address))
      country <- OptionT(findCountry(address.addressId))
      code <- OptionT(Future.successful(country.code))
    } yield code

    result.run
  }

//  def getCountryCode(personId: String): Future[Option[String]] = {
//
//    val result: Result[String] = for {
//      person <- ? <~ findPerson(personId)
//      address <- ? <~ person.address
//      country <- ? <~ findCountry(address.addressId)
//      code <- ? <~ country.code
//    } yield code
//
//    result.run
//
//  }

}
