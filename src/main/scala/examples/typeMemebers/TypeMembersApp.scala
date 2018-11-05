package examples.typeMemebers

object TypeMembersApp extends App {

  //The intui􏰀ve take-away from this is that type parameters are useful as “inputs” and type members are useful as “outputs”.

  //Enforce a type to be applicative to SOME TYPES only
  //suppose that we want to limit to the type of numbers but this trait is out of our control
  trait Mlist {
    type A //Abstract type member

    def head: A
    def tail: Mlist
  }

  //solution to limit the type
  trait ApplicableToNumbers {
    type A <: Number //upper bound
  }

  //Will fail on compile because string is not compatible with type definition
//  class CustomList(hd: String, tl: CustomList) extends Mlist with ApplicableToNumbers {
//    override type A = String
//    override def head = hd
//    override def tail = tl
//  }

  class IntList(hd: Int, tl: IntList) extends Mlist {
    override type A = Int
    override def head = hd
    override def tail = tl
  }


  //Abstract type parameter vs generic type parameter:
  //in fact is, two different ways to achieve the same goal.
  //in practice, when you [use type parameterization] with many different things, it leads to an explosion of parameters, and usually, what's more, in bounds of parameters.
}
