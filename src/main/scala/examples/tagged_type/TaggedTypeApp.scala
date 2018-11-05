package examples.tagged_type

import scalaz._

object TaggedTypeApp extends App {
  trait PersonIdTag

  type PersonId = String @@ PersonIdTag

  case class Person(id: PersonId, name: String)

  def genPersonId[A](id: A): A @@ PersonIdTag = Tag[A, PersonIdTag](id)

  val id: PersonId = genPersonId("123")
  val p = Person(id, "Artin")
//  val p2 = Person("ddd", "Artin")
}
