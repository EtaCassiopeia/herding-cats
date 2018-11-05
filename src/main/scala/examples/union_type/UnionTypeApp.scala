package examples.union_type

object UnionTypeApp extends App {

  //with implicit evidence
  def f[A](a: A)(implicit ev: (Int with String) <:< A) = a match {
    case i: Int    => i + 1
    case s: String => s.length
  }

  println(f(10))
  println(f("fff"))
  //println(f(true))


  /*
  class StringOrInt[T]
  object StringOrInt {
  implicit object IntWitness extends StringOrInt[Int]
  implicit object StringWitness extends StringOrInt[String]
}

object Bar {
  def foo[T: StringOrInt](x: T) = x match {
    case _: String => println("str")
    case _: Int => println("int")
  }
}
   */

  type ¬[A] = A => Nothing
  //using De Morgan's law this allows him to define union types
  type ∨[T, U] = ¬[¬[T] with ¬[U]]

  type ¬¬[A] = ¬[¬[A]]
  type |∨|[T, U] = { type λ[X] = ¬¬[X] <:< (T ∨ U) }

  def size[T: (Int |∨| String)#λ](t: T) = t match {
    case i: Int     => i
    case s: String  => s.length
    case b: Boolean => 1
  }

  println(size(10))
  println(size("hello"))

  //using Dotty
  /*
  def foo(xs: (String | Int)*) = xs foreach {
   case _: String => println("str")
   case _: Int => println("int")
}
 */

}
