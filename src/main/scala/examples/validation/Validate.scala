package examples.validation

import scalaz._
import Scalaz._
import examples.validation.Validate.ValFunc

import scala.language.higherKinds

case class Validate[F[_]: Monad, T](data: T)(valFuncs: ValFunc[F, T]*) {

  def andThen[B](action: T => F[String \/ B]): F[String \/ B] = {

    val validationResult: F[List[Option[String]]] =
      valFuncs.map(f => f(data)).toList.sequence

    val errorsMaybe: F[Option[String]] = validationResult.map { l =>
      val errors = l.flatten
      errors.nonEmpty option errors.mkString("\n")
    }

    errorsMaybe.flatMap(
      errors => errors ? Applicative[F].pure(errors.get.left[B]) | action(data)
    )
  }

}

object Validate {

  type ValFunc[F[_], T] = T => F[Option[String]]

}
