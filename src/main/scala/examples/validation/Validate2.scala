package examples.validation

import scalaz.{ValidationNel, _}
import Scalaz._
import Validate2._

import scala.language.higherKinds

case class Validate2[F[_]: Monad, T](data: T)(valFuncs: (T => Va[T])*) {

  def andThen[B](action: T => F[String \/ B]): F[String \/ B] = {

    val validators: Seq[ReaderT[Va, T, T]] =
      valFuncs.map(f => Kleisli(f))

    /**
    the <=< function is just like composition, only instead of working for normal functions like A -> B, it works for monadic functions like A -> M[B].
    alias for `andThen` */
    val va: ReaderT[Va, T, T] =
      validators.foldLeft(validators.head)((a, b) => a >=> b)

    va(data).toEither match {
      case Left(value) =>
        Applicative[F].pure(value.toList.mkString(",").left[B])
      case Right(value) => action(value)
    }
  }

}

object Validate2 {
  type Va[A] = ValidationNel[String, A]

  implicit val validationBind: Bind[Va] = new Bind[Va] {
    override def bind[A, C](fa: Va[A])(f: A => Va[C]): Va[C] = fa match {
      case Success(a) => f(a)
      case Failure(e) => Failure(e)
    }

    override def map[A, C](fa: Va[A])(f: A => C): Va[C] = fa match {
      case Success(a) => Success(f(a))
      case Failure(e) => Failure(e)
    }
  }
}
