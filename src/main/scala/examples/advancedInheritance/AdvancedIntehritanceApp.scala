package examples.advancedInheritance

object AdvancedIntehritanceApp extends App {
  trait Animal { def name: String }
  trait Lion extends Animal {
    override def name: String = "lion"
  }
  trait Tiger extends Animal {
    override def name: String = "Tiger"
  }

  class Mutant extends Tiger with Lion

  /*class Muant extends Animal with {
    override def name: String = "Tiger"} with { override def name: String = "Lion" }*/ //Peeks right most implementation
  val m = new Mutant
  println(m.name)
}
