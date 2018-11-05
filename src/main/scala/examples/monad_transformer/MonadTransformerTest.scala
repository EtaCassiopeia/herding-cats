package examples.monad_transformer

import scala.concurrent.Future
import cats.data.OptionT
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

object MonadTransformerTest extends App {
//The context we’re comprehending over must stay the same

  //nested contexts

  /*def getA: Future[Option[Int]] = ???
  def getB: Future[Option[Int]] = ???
  for {
    a <- getA
    b <- getB
  } yield a * b //error: value * is not a member of Option[Int]*/

  //Here we want for to take care of the outer context and let us write our code on the inner Option.
  //Hiding the outer context is exactly what a monad transformer does. Cats and Scalaz provides implementations for Option and Either named OptionT and EitherT respectively
  //This changes the context of the for from Future[Option[_]] to OptionT[Future, _].

  def getA: Future[Option[Int]] = ???
  def getB: Future[Option[Int]] = ???
  val result: OptionT[Future, Int] = for {
    a <- OptionT(getA)
    b <- OptionT(getB)
  } yield a * b

  val completedResult: Future[Option[Int]] = result.value

  val greetingFO: Future[Option[String]] = Future.successful(Some("Hello"))

  val firstnameF: Future[String] = Future.successful("Jane")

  val lastnameO: Option[String] = Some("Doe")

  //The monad transformer also allows us to mix Future[Option[_]] calls with methods that just
  //return plain Future via .liftM[OptionT]

  //and we can mix with methods that return plain Option

  val ot: OptionT[Future, String] = for {
    g <- OptionT(greetingFO)
    f <- OptionT.liftF(firstnameF)
    l <- OptionT.fromOption[Future](lastnameO)
  } yield s"$g $f $l"

  val result2
    : Future[Option[String]] = ot.value // Future(Some("Hello Jane Doe"))


  //the |> operator, which applies the function on the right to the value on the left

  /*
   def liftFutureOption[A](f: Future[Option[A]]) = OptionT(f)
  def liftFuture[A](f: Future[A]) = f.liftM[OptionT]
  def liftOption[A](o: Option[A]) = OptionT(o.pure[Future])
  def lift[A](a: A)               = liftOption(Option(a))

  val result = for {
         a <- getA |> liftFutureOption
         b <- getB |> liftFutureOption
         c <- getC |> liftFuture
         d <- getD |> liftOption
         e <- 10   |> lift
} yield e * (a * b) / (c * d)
   */



  //|> is often called the thrush operator because of its uncanny resemblance to the cute bird. Those who do not like symbolic operators can use the alias .into.

}
