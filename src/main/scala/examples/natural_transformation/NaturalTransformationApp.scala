package examples.natural_transformation

import scalaz._

object NaturalTransformationApp extends App {
  /*
  A function from one type to another is written as A => B in Scala, which is syntax sugar for a Function1[A, B].
  Scalaz provides similar syntax sugar F ∼> G for functions over type constructors F[_] to G[_].
   */

  val convert = new (IList ~> List) {
    override def apply[A](fa: IList[A]): List[A] = fa.toList
  }

  val list: List[Int] = convert(IList(1, 2, 3))

  //Or, more concisely, making use of kind-projector’s syntax sugar:
  //val convert = λ[IList ~> List](_.toList)
}
