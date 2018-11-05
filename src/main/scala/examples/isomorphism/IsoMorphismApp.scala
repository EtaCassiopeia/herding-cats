package examples.isomorphism

import scalaz._
import Isomorphism._
import examples.isomorphism.IsoMorphismApp.Library1.Countries.Country
import examples.isomorphism.IsoMorphismApp.Library2
import examples.isomorphism.IsoMorphismApp.Library2.Countries.Country

object IsoMorphismApp extends App {

  /*
  Sometimes we have two types that are really the same thing, causing compatibility problems because the compiler doesn’t know what we know.
  This typically happens when we use third party code that is the same as something we already have.
  This is when Isomorphism can help us out. An isomorphism defines a formal “is equivalent to” relationship between two types.
   */

  val listIListIso: List <~> IList =
    new IsoFunctorTemplate[List, IList] {
      def to[A](fa: List[A]): IList[A] = IList.fromList(fa)
      def from[A](fa: IList[A]): List[A] = fa.toList
    }

  //If we introduce an isomorphism, we can generate many of the standard typeclasses. For example
  trait IsomorphismSemigroup[F, G] extends Semigroup[F] {
    implicit def G: Semigroup[G]
    def iso: F <=> G
    def append(f1: F, f2: => F): F = iso.from(G.append(iso.to(f1), iso.to(f2)))
  }

  object Library1 {
    object Countries extends Enumeration {
      type Country = Value
      val AA, RU, IT, UA, GE, PK, KZ, AM, UZ, TJ, BD, KG, DZ, ZZ = Value
    }
  }

  object Library2 {
    object Countries extends Enumeration {
      type Country = Value
      val AA, RU, IT, UA, GE, PK, KZ, AM, UZ, TJ, BD, KG, DZ, ZZ = Value
    }
  }

  implicit val listIListIsoSet
    : Library1.Countries.Country <=> Library2.Countries.Country =
    new IsoSet[Library1.Countries.Country, Library2.Countries.Country] {
      override def to
        : Library1.Countries.Country => Library2.Countries.Country =
        country => Library2.Countries.withName(country.toString)

      override def from
        : Library2.Countries.Country => Library1.Countries.Country =
        country => Library1.Countries.withName(country.toString)
    }

  def process(country: Library2.Countries.Country)(
      implicit iso: Library1.Countries.Country <=> Library2.Countries.Country)
    : Library1.Countries.Country = iso.from(country)

  println(process(Library2.Countries.RU))

}
