package examples.datatype.writer

import cats._
import cats.data._
import cats.implicits._
import examples._

object WriterTypeApp extends App {
  //Whereas the Maybe monad is for values with an added context of failure, and the list monad is for nondeterministic values,
  //Writer monad is for values that have another value attached that acts as a sort of log value.


  //When using the Writer monad, you have to be careful which monoid to use, because using lists can sometimes turn out to be very slow.
  //That’s because lists use ++ for mappend and using ++ to add something to the end of a list is slow if that list is really long.

  def gcd(a: Int, b: Int): Writer[Vector[String], Int] = {
    if (b == 0) for {
      _ <- Writer.tell(Vector("Finished with " + a.show))
    } yield a
    else
      Writer.tell(Vector(s"${a.show} mod ${b.show} = ${(a % b).show}")) flatMap {
        _ =>
          gcd(b, a % b)
      }
  }

  gcd(12, 16).run.print()


  def logNumber(x: Int): Writer[List[String], Int] =
    Writer(List("Got number: " + x.show), 3)

  def multWithLog: Writer[List[String], Int] =
    for {
      a <- logNumber(3)
      b <- logNumber(5)
    } yield a * b

  multWithLog.run.print()
}
