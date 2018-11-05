package examples.datatype.state

import java.util.concurrent.atomic.AtomicLong

import cats._
import cats.implicits._
import cats.data._
import examples._

object StateApp extends App {
  //The goal is to automate the explicit passing of the states.
  //We’ll say that a stateful computation is a function that takes some state and returns a value along with some new state.
  //That function would have the following type:
  //
  //s -> (a, s)
  //State[S, A] is basically a function S => (S, A), where S is the type that represents your state and A is the result the function produces.
  //In addition to returning the result of type A, the function returns a new S value, which is the updated state.

  val nextElementId: State[AtomicLong, Long] = State { counter =>
    val elementId = counter.incrementAndGet()
    (new AtomicLong(elementId), elementId)
  }

  val elements=for {
    id <- nextElementId
    iconElement=s"iconElement$id"
    id2<- nextElementId
    titleElement = s"titleElement$id2"
  } yield List(iconElement,titleElement)

  elements.runA(new AtomicLong(0)).value.print()

  //Luckily, State[S, A] is an alias for StateT[Eval, S, A] - a monad transformer defined as StateT[F[_], S, A].
  //This data type represents computations of the form S => F[(S, A)].
}
