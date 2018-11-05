package examples.monad

import cats._
import cats.implicits._
import cats.data._
import cats.syntax._
import examples._

object MonadApp extends App {
  /*
Monads are a natural extension applicative functors, and they provide a solution to the following problem:
If we have a value with context, m a, how do we apply it to a function that takes a normal a and returns a value with a context.
   */

  (Right(3): Either[String, Int]).flatMap(x => Right(x + 1)).print()

  ("Hello".some map (s => s + "!")).print()

  //Apply
  (((x: String) => x + "!").some ap "Hello".some).print()

  //FlatMap
  ("Hello".some flatMap (x => (x + "!").some)).print()

  //Monad is a FlatMap with pure.
  type Birds = Int
  case class Pole(left: Birds, right: Birds) {
    def landLeft(n: Birds): Option[Pole] =
      if (math.abs((left + n) - right) < 4) copy(left = left + n).some
      else none[Pole]
    def landRight(n: Birds): Option[Pole] =
      if (math.abs(left - (right + n)) < 4) copy(right = right + n).some
      else none[Pole]
  }

  //>>= flatMap
  val rlr = Monad[Option].pure(Pole(0, 0)) >>= { _.landRight(2) } >>= {
    _.landLeft(2)
  } >>= { _.landRight(2) }

  rlr.print()

  def routine: Option[Pole] =
    for {
      start <- Monad[Option].pure(Pole(0, 0))
      first <- start.landLeft(2)
      second <- first.landRight(2)
      third <- second.landLeft(1)
    } yield third

  ((List(1, 2, 3), List(10, 100, 100)) mapN { _ * _ }).print()

  (List(3, 4, 5) >>= { x =>
    List(x, -x)
  }).print()

  (for {
    n <- List(1, 2)
    ch <- List('a', 'b')
  } yield (n, ch)).print()

  List(1, 2).zip(List('a', 'b')).print()

  //mapFilter, filter, collect
  (for {
    x <- (1 to 50).toList if x.show contains '7'
  } yield x).print()

  (1 to 50).toList.filter(_.show contains '7').print()

  (1 to 50).toList.collect { case x if x.show contains '7' => x }.print()


  import cats.mtl._, cats.mtl.implicits._

  def filterSeven[F[_]: FunctorEmpty](f: F[Int]): F[Int] =
    f filter { _.show contains '7' }

  filterSeven((1 to 50).toList).print()

 //Here’s a problem that really lends itself to being solved with non-determinism. Say you have a chess board and only one knight piece on it. We want to find out if the knight can reach a certain position in three moves.
  case class KnightPos(c: Int, r: Int) {
    def move: List[KnightPos] =
      for {
        KnightPos(c2, r2) <- List(KnightPos(c + 2, r - 1), KnightPos(c + 2, r + 1),
          KnightPos(c - 2, r - 1), KnightPos(c - 2, r + 1),
          KnightPos(c + 1, r - 2), KnightPos(c + 1, r + 2),
          KnightPos(c - 1, r - 2), KnightPos(c - 1, r + 2)) if ((1 to 8).toList contains c2) && ((1 to 8).toList contains r2)
      } yield KnightPos(c2, r2)
    def in3: List[KnightPos] =
      for {
        first <- move
        second <- first.move
        third <- second.move
      } yield third
    def canReachIn3(end: KnightPos): Boolean = in3 contains end
  }


  (KnightPos(6, 2) canReachIn3 KnightPos(6, 1)).print()

}
