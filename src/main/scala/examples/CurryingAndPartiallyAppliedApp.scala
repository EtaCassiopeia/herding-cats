package examples

object CurryingAndPartiallyAppliedApp extends App{

  def add(x:Int, y:Int) = x + y
  //And after currying:
  def addCurried(x:Int) = (y:Int) => x + y

  //Applied to Existing Methods
  //val addCurried = Function.curried(add _)

  /*
A curried would take, for example, a function (A, B) => C and turn it into A => B => C.
The actual currying and the resulting function depends on the amount of parameters.
And currying for only one parameter is not defined, as just results in the same function.
So, curried is not defined on the Function object, but on Function2, Function3, and so on. That means, you want to write:
   */

  def cat(s1: String, s2: String): String = (s1 + " " + s2).toUpperCase
  val curry: String => String => String = (cat _).curried   //cat _ :EtaExpansion (convert a method to Function, in this case Function2 which supports curried
  println(curry("short")("pants"))

  //Partially Applied Functions
  def process[A](filter:A=>Boolean)(list:List[A]):List[A] = {
    lazy val recurse = process(filter) _

    list match {
      case head::tail => if (filter(head)) {
        head::recurse(tail)
      } else {
        recurse(tail)
      }

      case Nil => Nil
    }
  }

  val even = (a:Int) => a % 2 == 0

  val numbersAsc = 1::2::3::4::5::Nil
  val numbersDesc = 5::4::3::2::1::Nil

  process(even)(numbersAsc).print()   // [2, 4]
  process(even)(numbersDesc).print()  // [4, 2]

  //Partials without Currying
  def addTriple(x:Int, y:Int, z:Int) = x + y + z

  val addFive: (Int, Int) => Int = addTriple(5, _:Int, _:Int) // val addFive = (a:Int, b:Int) => add(5, a, b)
  addFive(3, 1).print()

  val addTripleCurried: Int => Int => Int => Int = (addTriple _).curried
  val addTripleFiveCurried: Int => Int => Int = addTripleCurried(5)
  addTripleFiveCurried(3)( 1).print()


  def filter(xs: List[Int], p: Int => Boolean): List[Int] =
    if (xs.isEmpty) xs
    else if (p(xs.head)) xs.head :: filter(xs.tail, p)
    else filter(xs.tail, p)

  def modN(n: Int)(x: Int) = (x % n) == 0

  val nums = List(1, 2, 3, 4, 5, 6, 7, 8)
  println(filter(nums, modN(2)))
  println(filter(nums, modN(3)))


}
