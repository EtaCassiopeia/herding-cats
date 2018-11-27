package examples.writer_monad
import scalaz._
import Scalaz._

object WriterMonadApp extends App {
  //A Writer is a monad that attaches a log or some other accumulated data to a value.
  //The WriterT monad transformer is typically for writing to a
  //journal.
  //The wrapped type is F[(W, A)] with the journal accumulated in W.

  //There are two associated monads, MonadTell and MonadListen
  //The most obvious example is to use MonadTell for logging, or audit reporting
  //A popular specialisation of WriterT is when the monad is Id, meaning the underlying run value is just a simple tuple (W, A).
  //  type Writer[W, A] = WriterT[Id, W, A]

  def gcd(a: Int, b: Int): Writer[List[String], Int] =
    if (b == 0) for {
      _ <- List("Finished with " + a.shows).tell
    } yield a
    else
      List(a.shows + " mod " + b.shows + " = " + (a % b).shows).tell >>= { _ =>
        gcd(b, a % b)
      }

  println(gcd(8, 3))

  /*
  When using the Writer monad, you have to be careful which monoid to use, because using lists can sometimes turn out to be very slow. That’s because lists use ++ for mappend and using ++ to add something to the end of a list is slow if that list is really long.
   */

  def gcdUsingVector(a: Int, b: Int): Writer[Vector[String], Int] =
    if (b == 0) for {
      _ <- Vector("Finished with " + a.shows).tell
    } yield a
    else
      for {
        result <- gcdUsingVector(b, a % b)
        _ <- Vector(a.shows + " mod " + b.shows + " = " + (a % b).shows).tell
      } yield result

  println(gcd(8, 3))
}
