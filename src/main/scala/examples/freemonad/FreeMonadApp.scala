package examples.freemonad

import cats.free.Free


//Create an ADT representing your grammar
sealed trait KVStoreA[A]
case class Put[T](key: String, value: T) extends KVStoreA[Unit]
case class Get[T](key: String) extends KVStoreA[Option[T]]
case class Delete(key: String) extends KVStoreA[Unit]

object FreeMonadApp extends App{
  //The whole purpose of functional programming isn’t to prevent side-effects,
  //it is just to push side-effects to the boundaries of your system in a well-known and controlled way.
  /*

A free monad is a construction which allows you to build a monad from any Functor. Like other monads, it is a pure way to represent and manipulate computations.

In particular, free monads provide a practical way to:

represent stateful computations as data, and run them
run recursive computations in a stack-safe way
build an embedded DSL (domain-specific language)
retarget a computation to another interpreter using natural transformations
   */


  //Free your ADT

//1. Create a Free type based on your ADT
  type KVStore[A] = Free[KVStoreA, A]

  //2. Create smart constructors using liftF
  import cats.free.Free.liftF


  // Put returns nothing (i.e. Unit).
  def put[T](key: String, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))

  // Get returns a T value.
  def get[T](key: String): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get[T](key))

  // Delete returns nothing (i.e. Unit).
  def delete(key: String): KVStore[Unit] =
    liftF(Delete(key))

  // Update composes get and set, and returns nothing.
  def update[T](key: String, f: T => T): KVStore[Unit] =
    for {
      vMaybe <- get[T](key)
      _ <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
    } yield ()

  //3. Build a program
  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      _ <- delete("tame-cats")
    } yield n

  //4. Write a compiler for your program (natural transformation)

  import cats.arrow.FunctionK
  import cats.{Id, ~>}
  import scala.collection.mutable

  def impureCompiler: KVStoreA ~> Id  =
    new (KVStoreA ~> Id) {

      // a very simple (and imprecise) key-value store
      val kvs = mutable.Map.empty[String, Any]

      def apply[A](fa: KVStoreA[A]): Id[A] =
        fa match {
          case Put(key, value) =>
            println(s"put($key, $value)")
            kvs(key) = value
            ()
          case Get(key) =>
            println(s"get($key)")
            kvs.get(key).map(_.asInstanceOf[A])
          case Delete(key) =>
            println(s"delete($key)")
            kvs.remove(key)
            ()
        }
    }

  //5. Run your program
  val result: Option[Int] = program.foldMap(impureCompiler)
  println(result)


  //6. Use a pure compiler (optional)
  import cats.data.State

  type KVStoreState[A] = State[Map[String, Any], A]
  val pureCompiler: KVStoreA ~> KVStoreState = new (KVStoreA ~> KVStoreState) {
    def apply[A](fa: KVStoreA[A]): KVStoreState[A] =
      fa match {
        case Put(key, value) => State.modify(_.updated(key, value))
        case Get(key) =>
          State.inspect(_.get(key).map(_.asInstanceOf[A]))
        case Delete(key) => State.modify(_ - key)
      }
  }

  val result2: (Map[String, Any], Option[Int]) = program.foldMap(pureCompiler).run(Map.empty).value
  println(result2)


  //Composing Free monads ADTs.

}
