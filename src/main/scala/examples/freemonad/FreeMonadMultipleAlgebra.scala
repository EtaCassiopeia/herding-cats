package examples.freemonad

object FreeMonadMultipleAlgebra {
  //If we wanted to make use of operations from both Action[A] and AdvancedAction[A], Cats offers a Coproduct type we can use, which is roughly an Either but for higher kinded types.
  // The idea is to use this to ‘merge’ the two algebras into a common type.
  //
  //type ActionOrAdvanced[A] = Coproduct[Action, AdvancedAction, A]
  //A reminder that the above is akin to saying Either[Action[A], AdvancedAction[A]] for any A.

  //Inject is a type class that allows us to embed one algebra within another.
  //An instance of Inject[Action, ActionOrAdvanced], for example, allows us to ‘lift’ instances of Action to type ActionOrAdvanced.
}
