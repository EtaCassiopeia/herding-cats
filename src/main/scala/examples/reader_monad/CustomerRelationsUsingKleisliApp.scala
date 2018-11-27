package examples.reader_monad

import scalaz._
import Scalaz._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

object CustomerRelationsUsingKleisliApp extends App {
  case class User(email: String)

  class EmailServer {
    def send(to: String, body: String): Future[Unit] = ???
  }


  /* Reader is a Kleisli chich uses Id as context ( E => Id[A] is the same as E => A)
  object Reader extends scala.Serializable {
    def apply[E, A](f: E => A): Reader[E, A] = Kleisli[Id, E, A](f)
  }
   */

  object UserNotifier {
    def notify(user: User, about: String): Kleisli[Future, EmailServer, Unit] =
      Kleisli { emailServer =>
        emailServer.send(user.email, about)
      }
  }

  class CustomerRelations {
    def allUsers: Future[List[User]] = ???

    def retainUsers(): Kleisli[Future, EmailServer, Unit] = {
      for {
        users <- allUsers.liftKleisli
        _ <- users.map(u => UserNotifier.notify(u, "Visit our site!"))
          .sequenceU
          .map(_ => ())
      } yield ()
    }
  }
}
