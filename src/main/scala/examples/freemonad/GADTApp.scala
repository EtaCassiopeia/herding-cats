package examples.freemonad

object GADTApp extends App {
  sealed trait Attribute[T]
  case class IntAttribute(i: Int) extends Attribute[Int]
  case class BoolAttribute(b: Boolean) extends Attribute[Boolean]

  object Test {
    def eval[T](e: Attribute[T]): T = e match {
      case IntAttribute(i)  => i.asInstanceOf[T]
      case BoolAttribute(b) => b.asInstanceOf[T]
    }
  }

  println(Test.eval(IntAttribute(10)))
  println(Test.eval(BoolAttribute(true)))
}
