package examples.freemonad

import cats.free.Free
import cats.{Id, ~>}
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

sealed trait External[A]
case class Tickets(count: Int) extends AnyVal
case class InvokeTicketingService(count: Int) extends External[Tickets]
case class UserTicketsRequest(ticketCount: Int)

object GetTicketsExampleApp extends App{

  // Free : free to be interpreted in any way
  //We will describe the external interactions as data using a family of case classes
  //You can view these classes a base instruction set of a domain-specific language.

  //Firstly, having this basic instruction set, we'd like to construct programs using them as primitives. Or rather, we want to construct descriptions of programs, and execute them later
  //Formally, we want to define a data structure Program[External, A] (parametrised by the type of the base instructions and the result of the whole program).
  //Secondly, having a description of a program, and an interpretation for our base instruction set, we'd like to extend this interpretation to cover the programs we have built.
  // More specifically, given a function interpretBase[A]: External[A] => M[A], where M is any monad, we'd like to extend this to a function interpretProgram[A]: Program[External, A] => M[A].
  // It would be nice if there was only one way to create such a (well-behaving) extension so that we don't have to make any additional choices.
  //
  //As you've probably guessed Program[External, A] = Free[External, A], that is it's the free monad over External

  //The free monad brings together two concepts, monads and interpreters, allowing the creation of composable monadic interpreters.
  //Concretely this means the free monad provides:
  //
  //an AST to express monadic operations;
  //an API to write interpreters that give meaning to this AST.

  //monads are fundamentally about control flow (using flatMap)

  // Interpreters are about separating the representation of a computation from the way it is run. Any interpreter has two parts4:
  //
  //an abstract syntax tree (AST) that represents the computation; and
  //a process that gives meaning to the abstract syntax tree. That is, the bit that actually runs it.

  def purchaseTickets(input: UserTicketsRequest): Free[External, Option[Tickets]] = {
    if (input.ticketCount > 0) {
      // creates a "Suspend" node
      Free.liftF(InvokeTicketingService(input.ticketCount)).map(Some(_))
    } else {
      Free.pure(None)
    }
  }

  def bonusTickets(purchased: Option[Tickets]): Free[External, Option[Tickets]] = {
    if (purchased.exists(_.count > 10)) {
      Free.liftF(InvokeTicketingService(1)).map(Some(_))
    } else {
      Free.pure(None)
    }
  }

  def formatResponse(purchased: Option[Tickets], bonus: Option[Tickets]): String =
    s"Purchased tickets: $purchased, bonus: $bonus"

  val input = UserTicketsRequest(11)

  val logic: Free[External, String] = for {
    purchased <- purchaseTickets(input)
    bonus <- bonusTickets(purchased)
  } yield formatResponse(purchased, bonus)

  val externalToServiceInvoker: ~>[External, Future] = new (External ~> Future) {
    override def apply[A](e: External[A]): Future[A] = e match {
      case InvokeTicketingService(c) => serviceInvoker.run(s"/tkts?count=$c")
    }
  }

  val result: Future[String] = logic.foldMap(externalToServiceInvoker)
  result.foreach(println)

  import scala.concurrent.duration._
  Await.result(result, 10 seconds)

  //Or in tests, we can provide an alternative interpretation
  val testingInterpeter = new (External ~> Id) {
    override def apply[A](e: External[A]): Id[A] = e match {
      case InvokeTicketingService(c) => Tickets(10)
    }
  }

  val testResult: String = logic.foldMap(testingInterpeter)
  println(testResult)

  object serviceInvoker {
    def run(path: String) = {
      Future {
        Tickets(11)
      }
    }
  }
}
