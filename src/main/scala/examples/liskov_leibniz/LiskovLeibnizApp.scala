package examples.liskov_leibniz

import scalaz._
import Scalaz._
import Liskov.<~<, Leibniz.===

object LiskovLeibnizApp extends App {
// Leskov <~< and Leibniz === are both type operation symbols, which are actually scalaz's own version of the type restriction operators <:< and =:=

  case class Foo[A](a: A) { // type A can be any type
    def getLength(implicit ev: A =:= String): Int = a.length // A must be String
    def getSquare(implicit ev: A <:< Int): Int =
      a * a // A must be an Int or subclass
  }
  Foo("word length").getLength //> res0: Int = 11
  Foo(3).getSquare //> res1: Int = 9
  //Foo("word length").getSquare //cannot prove that String <:< Int
  //Foo(3).getLength

  case class Bar[A](a: A) { // type A can be any Type
    def getLength(implicit ev: A === String): Int =
      ev(a).length // A must be String
    def getSquare(implicit ev: A <~< Int): Int =
      ev(a) * ev(a) // A must be an Int or subclass
  }

  Bar(" word length ").getLength //> res0: Int = 11
  Bar(3).getSquare //> res1: Int = 9
  //Bar("word length").getSquare //could not find implicit value for parameter ev: scalaz.Liskov.<~<[String,Int]
  //Bar(3).getLength

  case class Baz[A <: String](a: A) { // type A is subtype of String
    def getLength: Int =
      a.length
  }

  Baz(" word length ").getLength //> res0: Int = 11
  //Baz(10).getLength

}
