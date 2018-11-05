package examples.path_dependent_type

object PathDependentTypeApp extends App {

  class Outer {
    class Inner

    def printInner(i: Inner) = ???

    def printGeneral(i: Outer#Inner) = ???
  }

  val o = new Outer
  val inner = new o.Inner

  o.printInner(inner) //works because

  val oo = new Outer
//  oo.printInner(inner) // does not work, because o.Inner and oo.Inner are different types

  //path dependent type
  oo.printGeneral(inner) //works fine

  //DB keyed by String or Int, but maybe others
  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // should be ok
  get[StringItem]("Scala") //should be ok

//  get[IntItem]("Scala") //should receive type mismatch error

}
