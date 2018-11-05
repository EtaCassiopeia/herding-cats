package examples.self_type

object SelfTypeApp extends App {
  //Requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => //whoever implements Singer must also to implement Instrumentalist as well
    //It is called SELF TYPE, name is optional. you can use whatever you want like `self`, `scala` and even `this`
    //We can use trait Singer extends Instrumentalist. but those are unrelated types. and it does not make sense

    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist { //Self type is a constraint which forbids us implementing the Singer without Instrumentalist
    override def play(): Unit = ???

    override def sing(): Unit = ???
  }

  //Inheritance vs self type
  class A
  class B extends A //B is an A

  class T
  class S { self: T => //S required T (S is not a T)
  }

  //CAKE PATTERN ==> "dependency injection"
  trait Repository {
    def store(x: Int): String
  }

  //layer 1
  trait SlickRepository extends Repository

  trait Service { self: Repository =>
    def storeValue(x: Int): String = store(x)
  }

  //layer 2
  trait DefaultService extends Service with SlickRepository

  trait Application { self: Service =>
  }

  //layer 3
  trait DefaultApp extends Application with DefaultService

  //cyclical dependencies
//  class X extends Y
//  class Y extends X


  //X adn Y are unrelated types but they go hand in hand, when someone extends one he should extends the other as well
  trait X { self: Y =>
  }
  trait Y { self: X =>
  }
}
