package examples.show

import cats._, cats.data._, cats.implicits._, cats.syntax._

object ShowApp extends App {

  case class Person(name: String, lastName: String)
  case class Employee(employeeId: String, details: Person)

  implicit val personShow: Show[Person] = Show.show[Person](_.name)

  val p = Person("Artin", "Zainalpour")
  val e = Employee("id1", p)

  println(implicitly[Show[Person]].show(p))
  println(p.show)

  implicit val employeeShow: Show[Employee] =
    personShow.contramap[Employee]( e => e.details)

  println(e.show)


  def intToString(a:Int):String = a.toString

  def print(s:String):Unit = println(s)

  val f1 = intToString _
  val f2 = print _

//  val f3=print compose f1
  val f3: Int => Unit =print _ compose intToString

  f3(10)

  val f4: Int => Unit =intToString _ andThen print
  f4(10)
}
