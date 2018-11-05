package examples

object EtaExpansionApp extends App {
//Methods as functions
  //“Eta-expansion converts an expression of method type to an equivalent expression of function type.”

  val nums = List(1, 2, 3)

  def isEven(i: Int) = i % 2 == 0 // define a method
  nums.filter(isEven).print() // pass the method into a function

  //How to manually convert a method to a function
  val isEvenFn: Int => Boolean = isEven _
  nums.filter(isEvenFn).print()

  //The effect of _ is to perform the equivalent of the following: we construct a Function1 instance that delegates to our method.
  val g = new Function1[Int, Boolean] { def apply(n: Int): Boolean = isEven(n) }

  //In contexts where the compiler expects a function type, the desired expansion is inferred and the underscore is not needed:
  List(1, 2, 3).map(isEven)
  //This applies in any position where a function type is expected, as is the case with a declared or ascribed type:
  val z: Int => Boolean = isEven

  //In the presence of overloading you must provide enough type information to disambiguate:
  val y: Int => String = "foo".substring: Int => String

  //Methods with multiple parameters expand to equivalent multi-parameter functions:
  def plus(a: Int, b: Int): Int = a + b
  val m: (Int, Int) => Int = plus
  //Perhaps surprisingly, such methods also need explicit η-expansion when partially applied:
  val pam: Int => Int = m(1, _: Int)

  //Methods with multiple parameter lists become curried functions:
  def plus2(a: Int)(b: Int): Int = a + b
  val n: Int => Int => Int = plus2
  //However partially applied curried functions do not need eta expansion.
  val pan: Int => Int = n(1)

  //Type Parameters
  //Values in scala cannot have type parameters; when η-expanding a parameterized method all type arguments must be specified
  def id[A](a: A): A = a
  val yId = id[Int] _

  //Implicit Parameters
  //Implicit parameters are passed at the point of expansion and do not appear in the type of the constructed function value:
  def foo[N: Numeric](n: N): N = n

  val withImplicitFoo: Int => Int = foo[Int] _

  def bar[N](n: N)(implicit ev: Numeric[N]): N = n

  val withImplicitBar = bar[Int] _

}
