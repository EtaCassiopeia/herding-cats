package examples.FBounded_polymorphism
import scala.collection.immutable

object FBoundedPolymorphismApp extends App {

  //Sometime we need to return something with the same type of the parent class
//  trait Animal[A <: Animal[A]] { //recursive type - F-Bounded Polymorphism (FBP)
//    def breed:List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ???
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }
//
//  //trait Entity[T <: Entity[T]] //ORM
//
////  other problem, how Do I make the compiler enforce that the class I am defining and the type A that I am annotating with are the same
//    class Crocodile extends Animal[Dog] {
//      override def breed: List[Animal[Dog]] = ???
//    }

  //Solution FBP + self-types
//  trait Animal[A <: Animal[A]] { self: A => //recursive type - F-Bounded Polymorphism (FBP) + self-type
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ???
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }
//
////  class Crocodile extends Animal[Dog] {
////    override def breed: List[Animal[Dog]] = ???
////  }

  //using type class
//  trait Animal
//
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  object Dog {
//    implicit object DogCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = ???
//    }
//  }
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[Any] =
//      canBreed.breed(animal)
//  }
//
//  val dog=new Dog
//  dog.breed

  //Other solution
  //using the above solution we split the API between Animal trait and the type class. Animal does not have any method
  //let simplify the implementation by making the Animal itself a type class

  trait Animal[A] {
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogCanBreed extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = ???
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[Any] =
      animalTypeClassInstance.breed(animal)
  }

    val dog=new Dog
    dog.breed

}
