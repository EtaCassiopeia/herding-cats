package examples.kleisli

import scalaz._
import Scalaz._

object KleisliApp extends App {

  /**
    * the <=< function is just like composition (>=>: andThen), only instead of working for normal functions like A -> B, it works for monadic functions like A -> M[B].
    */
  //Scalaz defines Reader as a special case of Kleisli
  // type ReaderT[F[+_], E, A] = Kleisli[F, E, A]
  val f: ReaderT[Option, Int, Int] = Kleisli { x: Int =>
    (x + 1).some
  }

  val g: ReaderT[Option, Int, Int] = Kleisli { x: Int =>
    (x * 100).some
  }

  val result = 4.some >>= (f >=> g) // >>= bind
  println(result)

  val composedFunction = f >=> g
  println(composedFunction(4))

  val addStuff: Reader[Int, Int] = for {
    a <- Reader { (_: Int) * 2 }
    b <- Reader { (_: Int) + 10 }
  } yield a + b

  println(addStuff(3))

  val addStuffKleisli: ReaderT[Option, Int, Int] = for {
    a <- ReaderT[Option, Int, Int] { x: Int =>
      (x * 2).some
    }
    b <- ReaderT[Option, Int, Int] { x: Int =>
      (x + 10).some
    }
  } yield a + b

  println(addStuffKleisli(3))

}
