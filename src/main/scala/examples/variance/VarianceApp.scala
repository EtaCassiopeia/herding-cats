package examples.variance

object VarianceApp extends App {
  //Variance: "inheritance" - The substitution of generics
  //Method Arguments are in covariant position (All the types which are used as parameter should be defined as contravariant [-T])
  //Method return type are in contravariant position (All the types which are used as return types should be defined as covariant[+A])

  //Check trait Function1 as a great example

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T] //An invariance List

  //Invariant implementation
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  //Covariant Version
  class CParking[+T](vehicles: List[T]) {
    def park[B >: T](vehicle: B): CParking[B] = ??? //widening type
    def impound[B >: T](vehicles: List[B]): CParking[B] = ???
    def checkVehicle(condition: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???

  }

  //Contravariant Version
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] =
      ??? //List is contravariant in the sense that it follows the variance of T
    def checkVehicle[S <: T](condition: String): List[S] =
      ??? //restrict the type

//    def flatMap[S](f: T => XParking[S]): XParking[S] = ???   //Function1[T,S]: Function1 in contravariant in T, and T itself is contravariant. It means "double" contravariant which is "covariant" in fact
    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???

  }

  /*
      Rule of thumb
      - Use covariance = COLLECTION OF THINGS
      - Use contravariance = GROUP OF ACTIONS
   */

  //In this example we more likely intended to use Parking as a set of actions. Therefore, it is better to implement it as contravariance

  //Use nn invariance List
  //Covariant Version
  class CParking2[+T](vehicles: IList[T]) {
    def park[B >: T](vehicle: B): CParking2[B] = ??? //widening type
    def impound[B >: T](vehicles: IList[B]): CParking2[B] = ???
    def checkVehicle[B >: T](condition: String): IList[B] = ???
  }

  //Contravariant Version
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] =
      ??? //List is contravariant in the sense that it follows the variance of T
    def checkVehicle[S <: T](condition: String): IList[S] =
      ??? //restrict the type
  }

}
