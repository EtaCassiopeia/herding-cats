package examples.reader_monad

import scalaz._
import Scalaz._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

object CustomerRelationsApp extends App{
  case class User(email: String)

  class EmailServer {
    def send(to: String, body: String): Future[Unit] = ???
  }

  object UserNotifier {
    def notify(user: User, about: String): Reader[EmailServer, Future[Unit]] =
      Reader { emailServer =>
        emailServer.send(user.email, about)
      }
  }

  class CustomerRelations {
    def allUsers: Future[List[User]] = ???

    def retainUsers(): Reader[EmailServer, Future[Unit]] = {
      allUsers
        .map {
          _.map(u => UserNotifier.notify(u, "Visit our site!"))
            .sequenceU
            .map(_.sequence.map(_ => ()))
        }
        .toReaderFunctor
        .map(_.join)
    }
  }

  implicit class RichFunctorReader[F[_]: Functor, A, B](fr: F[Reader[A, B]]) {
    def toReaderFunctor: Reader[A, F[B]] = Reader { a => fr.map(_.run(a)) }
  }

}
