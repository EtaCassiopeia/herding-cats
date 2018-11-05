package examples.structural_types

import scala.language.reflectiveCalls

object StructuralTypeApp extends App { //CAVEAT: based on reflection, has a big impact on performance. Only use it when it's required
  type JavaClosable = java.io.Closeable

  class HipsterClosable {
    def close(): Unit = ???
  }

  //Structual type
  type UnifiedClosable = { def close(): Unit }

  def closeQuietly(unifiedClosable: UnifiedClosable): Unit =
    unifiedClosable.close()

  //TYPE REFINEMENTS
//Use structural type to refine the existing type
  type AdvancedClosable =
    JavaClosable { //new type: enriched JavaClosable with a new method
      def closeSilently(): Unit
    }

  class AdvancedJavaClosable extends JavaClosable {
    override def close(): Unit = ???
    def closeSilently(): Unit = ???
  }

  def closeShh(closable: AdvancedJavaClosable) = closable.close()

  closeShh(new AdvancedJavaClosable)

  //Using structural type as standalone type
  def altClose(closable: { def close(): Unit }) = closable.close()

  //type-checking => duck typing
  //static duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = ???
  }

  class Car {
    def makeSound(): Unit = ???
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  //Exercise

  trait CBL[T] {
    def head: T
    def tail: CBL[T]
  }

  class Brain

  class Human {
    def head: Brain = ???
  }

  def f[T](somethingWithHead: { def head: T }) = println(somethingWithHead.head)

  f(new Human)
  f(new CBL[Int] {
    override def head: Int = ???

    override def tail: CBL[Int] = ???
  })

  //
  object HeadEqualizer {
    type Headable[T] = { def head: T } //type can be parameterized as well

    def ===[T](a: Headable[T], b: Headable[T]) = a.head == b.head //This is not type-safe, because it uses reflections.
    // Reflection can easily remove the type T, so this method can accept anything with a head type
  }
}
