package examples.monad_transformer

import scala.concurrent.{Await, Future}
import scalaz._
import Scalaz._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.higherKinds
import scala.util.Try

object MaybeTApp extends App {
  //Monad transformers are data structures that wrap an underlying value and provide a monadic effect.
  //A monad transformer is similar to a regular monad, but it’s not a standalone entity: instead, it modifies the behaviour of an underlying monad

  case class User(name: String, age: Int)

  trait Twitter[F[_]] {
    def getUser(name: String): F[Maybe[User]]
    def getStars(user: User): F[Int]
  }

  val mockEitherTwitter =
    new Twitter[String \/ ?] {
      def getUser(name: String): String \/ Maybe[User] =
        if (name.contains(" ")) Maybe.empty[User].right[String]
        else if (name === "wobble") "connection error".left
        else User(name, 10).just.right
      def getStars(user: User): String \/ Int =
        if (user.name.startsWith("w")) 10.right
        else "stars have been replaced by hearts".left
    }

  val mockFutureTwitter: Twitter[Future] = new Twitter[Future] {
    def getUser(name: String): Future[Maybe[User]] =
      if (name.contains(" ")) Future.successful(Maybe.empty[User])
      else if (name === "wobble") Future.failed(new NoSuchElementException)
      else Future.successful(User(name, 10).just)
    def getStars(user: User): Future[Int] =
      if (user.name.startsWith("w")) Future.successful(10)
      else Future.failed(new NoSuchElementException)
  }

  def stars[F[_]: Monad](t: Twitter[F], name: String): F[Maybe[Int]] = {
//    val s: F[Maybe[Int]] =
//      t.getUser(name) >>= (maybe => maybe.traverse(u => t.getStars(u)))

    for {
      maybeUser <- t.getUser(name)
      maybeStars <- maybeUser.traverse(t.getStars) //To handle the Empty case
    } yield maybeStars
  }

  //By having MonadPlus in  context:
  def starsMonadPlus[F[_]: MonadPlus](t: Twitter[F], name: String): F[Int] =
    for {
      user <- t.getUser(name) >>= (_.orEmpty[F])
      stars <- t.getStars(user)
    } yield stars

  //using Monad Transformer
  def starsMaybeT[F[_]: Monad](t: Twitter[F], name: String): MaybeT[F, Int] =
    for {
      user <- MaybeT(t.getUser(name))
      stars <- t.getStars(user).liftM[MaybeT]
    } yield stars

  //MonadError
  def starsMonadError[F[_]](t: Twitter[F], name: String)(
      implicit F: MonadError[F, String]): F[Int] =
    for {
      user <- t.getUser(name) >>= (_.orError(s"user '$name' not found")(F))
      stars <- t.getStars(user)
    } yield stars

  //EitherT
  def starsEitherT[F[_]: Monad](t: Twitter[F],
                                name: String): EitherT[F, String, Int] =
    for {
      user <- EitherT(t.getUser(name).map(_ \/> s"user '$name' not found")) //Turn the underlying value into a right disjunction if present, otherwise return a left disjunction with the provided fallback value
      stars <- EitherT.rightT(t.getStars(user))
    } yield stars

  println(
    Try(
      Await.result(starsEitherT(mockFutureTwitter, "Foo Bar").run, 5 seconds)))
  println(
    Try(Await.result(starsEitherT(mockFutureTwitter, "wibble").run, 5 seconds)))
  println(
    Try(Await.result(starsEitherT(mockFutureTwitter, "wobble").run, 5 seconds)))

}
