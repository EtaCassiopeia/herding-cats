package examples.freemonad

import scalaz._
import Scalaz._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.language.higherKinds

case class Ticket(price: Int)

//Life without free monad
trait TicketingService[M[_]] {

  implicit def m
    : Monad[M] //trait does not supports context bound for parameter type

  def invoke(count: Int): M[Ticket]

}

class AsyncTicketingService extends TicketingService[Future] {
  override implicit def m: Monad[Future] = implicitly[Monad[Future]]

  override def invoke(count: Int): Future[Ticket] = ???
}
