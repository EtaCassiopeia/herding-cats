package examples.freemonad

import cats.Functor
import cats.free.Coyoneda


object YonedaCoyonedaApp extends App {

  //we can use Yoneda to fuse multiple maps into one, Yoneda needs a Functor.
  //If we need to use this concept on a data type which is not a Functor we have to overcome the restrictions on our data type to be a Functor by creating PretendFunctor (Coyoneda).
  //Pretending to have a functor for any F is a really useful abstraction. You can “make” your plain data structure an Applicative or a Monad even if it’s not even a Functor.
  //This trick is used in Free structures implementation, which is a very useful concept that can change the way we construct and compose and our programs.
  //Coyoneda to automatically convert a type constructor into a functor that the free monad requires

  // our plain data structure
  case class Person[A](a: A)

  val personCoyo0: Coyoneda[Person, Int] = Coyoneda(Person(42))(identity)
  val personCoyo1: Coyoneda[Person, Int] = personCoyo0.map(_ + 1).map(_ + 2).map(_ + 3) // silly example
  // nothing is executed until next line

  // lets define functor for Person
  val personFunctor = new Functor[Person] {
    override def map[A, B](fa: Person[A])(f: A => B): Person[B] = Person(f(fa.a))
  }

  // and then pass it to Coyoneda
  val person: Person[Int] = personCoyo1.run(personFunctor)
  println(person) // should yield Person(48)

}
