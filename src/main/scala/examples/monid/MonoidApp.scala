package examples.monid

import cats._
import cats.implicits._
import examples._

object MonoidApp extends App {
  //Monoid is a semigroup with pure function

  case class WrappedInt(value: Int) extends AnyVal
  (none[String] |+| "test".some).print()

  case class First[A: Eq](val unwrap: Option[A])
  object First {
    implicit def firstMonoid[A: Eq]: Monoid[First[A]] = new Monoid[First[A]] {
      def combine(a1: First[A], a2: First[A]): First[A] =
        First((a1.unwrap, a2.unwrap) match {
          case (Some(x), _) => Some(x)
          case (None, y)    => y
        })
      def empty: First[A] = First(None: Option[A])
    }
    implicit def firstEq[A: Eq]: Eq[First[A]] = new Eq[First[A]] {
      def eqv(a1: First[A], a2: First[A]): Boolean =
        Eq[Option[A]].eqv(a1.unwrap, a2.unwrap)
    }
  }

  (First('a'.some) |+| First('b'.some)).print()
  (First(none[Char]) |+| First('b'.some)).print()
  (First(none[Char]) |+| First(none[Char]) |+| First('b'.some)).print()

  List(none[Char], none[Char], 'b'.some).find(_.isDefined).print()


  case class Last[A: Eq](val unwrap: Option[A])
  object Last {
    implicit def lastMonoid[A: Eq]: Monoid[Last[A]] = new Monoid[Last[A]] {
      def combine(a1: Last[A], a2: Last[A]): Last[A] =
        Last((a1.unwrap, a2.unwrap) match {
          case (_, Some(y)) => Some(y)
          case (x, None)    => x
        })
      def empty: Last[A] = Last(None: Option[A])
    }
    implicit def lastEq[A: Eq]: Eq[Last[A]] = new Eq[Last[A]] {
      def eqv(a1: Last[A], a2: Last[A]): Boolean =
        Eq[Option[A]].eqv(a1.unwrap, a2.unwrap)
    }
  }


  (Last('a'.some) |+| Last('b'.some)).print()
  (Last('a'.some) |+| Last(none[Char])).print()

  List('a'.some, 'b'.some).filter(_.isDefined).takeRight(1).print()


}
