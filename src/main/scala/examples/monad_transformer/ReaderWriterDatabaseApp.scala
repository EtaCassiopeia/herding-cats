package examples.monad_transformer

import scalaz.Reader

object ReaderWriterDatabaseApp extends App {

  type Key = String

  trait Transaction

  /* Work represents a unit of work to do against the Database
   * It is a type alias for a scalaz.Reader, which wraps
   * a Transaction => A
   */
  type Work[A] = Reader[Transaction, A]

  object Database {

    object MyTransaction extends Transaction

    // Run now requires Work
    def run[T](work: Work[T]): T =
      work.run(MyTransaction)

    // lift operations into Work - they do nothing here
    def put[A](key: Key, a: A): Work[Unit] =
      Reader(Transaction => {})

    def find[A](key: Key): Work[Option[A]] =
      Reader(Transaction => None)
  }

  // the program
  val work: Work[Option[String]] =
    for {
      _ <- Database.put("foo", "Bar")
      found <- Database.find[String]("foo")
    } yield found

  // now run the program
  val result: Option[String] = Database.run(work)
}
