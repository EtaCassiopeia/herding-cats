package examples.applicative
import cats._
import cats.implicits._
import cats.syntax._
import examples._

import scala.collection.immutable

object ApplicativeApp extends App {
  //Then applicative functors; a pretty cool thing that makes monadic-style operations parallelizable, or, rather, independent.
  //Applicative is an extension for Apply with pure method (<*> ap)
  val o1: Option[Int] = Applicative[Option].pure(1)
  Applicative[Option]
    .ap(((_: Int) * 3).some)(Applicative[Option].pure(2))
    .print()

  ((_: Int) * 3).some.ap(2.some).print()

  def isAutoFollowed(officialAccountId: String): Boolean = false

  val subscribedChannels: List[Unit] =
    Applicative[List]
      .whenA(isAutoFollowed("official-account-id"))( //otherwise return empty list (pure)
        List("subscribed-channel1", "subscribed-channel2"))

  //Let’s try implementing a function that takes a list of applicatives and returns an applicative that has a list as its result value. We’ll call it sequenceA.
  def sequenceA[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] =
    list match {
      case Nil     => Applicative[F].pure(Nil: List[A])
      case x :: xs => (x, sequenceA(xs)) mapN { _ :: _ }
    }

  val result1: Option[List[Int]] = sequenceA(List(1.some, 2.some))

  val result2: Option[List[Int]] =
    List(1.some, 2.some).sequence[Option, Int] //cats Traverse

  val result3: Option[List[Int]] = List(1, 2).traverse(_.some)
  val result4: Option[List[Int]] =
    List(1, 2).map(_.some).sequence[Option, Int]

  val result5: List[List[Int]] = sequenceA(List(List(1, 2, 3), List(4, 5, 6)))
  result5.print()

  val result6: List[(Int, Int)] = List(1, 2, 4).zip(List(4, 5, 6))
  result6.print()

  //Using sequenceA is useful when we have a list of functions and we want to feed the same input to all of them and then view the list of results.
  val f = sequenceA[Function1[Int, ?], Int](
    List((_: Int) + 3, (_: Int) + 2, (_: Int) + 1))
  val result7: List[Int] = f(3)

  result7.print()

}
