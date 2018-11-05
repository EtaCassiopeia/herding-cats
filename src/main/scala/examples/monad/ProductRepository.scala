package examples.monad

import cats.Monad
import cats.implicits._

trait ProductRepository[M[_]] {
  def findProduct(productId: String): M[Option[String]]
}

class Program[M[_]: Monad](repo: ProductRepository[M]) {
  def rename(): M[Option[String]] =
    repo.findProduct("dd").flatMap { a =>
      Monad[M].pure(Option(a.toString))
    }
}
