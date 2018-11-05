package examples.higher_kinded_type

object HigherKindedTypeApp extends App {

  //One of the more powerful features Scala has is the ability to generically abstract across things that take type parameters.
  //This feature is known as Higher Kinded Types (HKT).

  //Essentially what HKT gives us is the ability to generalize across type constructors

  //where a type constructor is anything that has a type parameter. For instance List[_]* is not a type, the underscore is a hole into which another type may be plugged, constructing a complete type.
  //List[String] and List[Int] being examples of complete (or distinct) types.

  //Kinds
  //Now that we have a type constructor we can think of several different kinds of them, classified by how many type parameters they take.
  //The simplest – like List[_] – that take a single param have the kind:

  //(* -> *)

  //This says: given one type, produce another. For instance, given String produce the type List[String].

  //Something that takes two parameters, say Map[_, _], or Function1[_, _] has the kind:
  //(* -> * -> *)
  //This says: given one type, then another, produce the final type. For instance given the key type Int and the value type String produce the type Map[Int, String].

  //Furthermore, you can have kinds that are themselves parameterized by higher kinded types. So, something could not only take a type, but take something that itself takes type parameters. An example would be the covariant functor: Functor[F[_]], it has the kind:
  //((* -> *) -> *)
  //This says: given a simple higher kinded type, produce the final type. For instance given a type constructor like List produce the final type Functor[List].

  //Strictly speaking, in Scala List[_] is actually an existential type

  trait Monad[F[_], A] {  //F is a one-argument type constructor
    def map[B](f: A => B): F[B]
    def flatMap[B](f: A => F[B]): F[B]
  }

  implicit class listMonad[A](list: List[A]) extends Monad[List, A] {
    override def map[B](f: A => B): List[B] = list.map(f)

    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
  }

  implicit class optionMonad[A](option: Option[A]) extends Monad[Option, A] {
    override def map[B](f: A => B): Option[B] = option.map(f)

    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
  }

  def multiply[F[_], A, B](implicit ma: Monad[F, A],
                           mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  println(multiply(List(1, 2), List("a", "b")))

}
