package object examples {
  implicit class withPrintln[A](a: A) {
    def print(): Unit = println(a)
  }
}
