package examples.monad_transformer
import scalaz._
import Scalaz._
//import scalaz.EitherT._
//import scalaz.MonadTrans._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

object TrivialMonadTransformerApp {
  case class User(id: String, name: String, age: Int)
  case class MyError(msg: String) extends AnyVal

  type ResultT[F[_], A] = EitherT[F, MyError, A]
  type FutureResult[A] = ResultT[Future, A]

  def checkUserExist(id: String): Future[\/[MyError, User]] = ???
  def checkCanBeUpdated(u: User): Future[Boolean] = ???
  def updateUserOnDB(u: User): Future[User] = ???

  def updateUser(user: User): FutureResult[User] =
    for {
      user <- EitherT(checkUserExist(user.id))
      _ <- EitherT.rightT(checkCanBeUpdated(user))
      updatedUser <- EitherT.rightT(updateUserOnDB(user))
//      updatedUser <- updateUserOnDB(user).liftM[EitherT]
    } yield updatedUser
}
