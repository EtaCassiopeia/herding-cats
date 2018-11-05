package examples.patternmatching

object PatternMatchingApp extends App {
  case class Element(title: String, `type`: String)

  object galleryElement {
//    def unapply(element: Element): Option[Boolean] =
//      if (element.`type` == "gallery")
//        Some(true)
//      else None

    val partialFunction: PartialFunction[Element, Boolean] = {
      case Element(_, t) if t == "galley" => true
    }

    val lifted: Element => Option[Boolean] = partialFunction.lift //lift a partialFunction to Option, convert a partialFunction to total function

    def unapply(element: Element): Option[Boolean] =
      lifted(element)
  }

  object titleElement {
    def unapply(element: Element): Boolean = element.`type` == "title"
  }

  object backgroundElement {
    def unapply(arg: Element): Option[(String, String)] =
      if (arg.`type` == "background") Some(arg.title, arg.`type`) else None
  }

  val elements: List[Element] = List(
    Element("title-element", "title"),
    Element("gallery-element", "gallery"),
    Element("background-element", "background"),
    Element("other-element", "other"))

  elements.foreach(println)

  elements
    .map {
      case galleryElement(_) => "it is galley element"
      case titleElement()    => "it is title element"
      case t backgroundElement typ =>
        s"it is background element $t $typ"
      case Element(t, e) => "it is normal element"
    }
    .foreach(println)

  //infix pattern
  //only works for types with two type arguments in unapply
  case class Or[A, B](a: A, b: B)

  val either = Or(1, "one")

  either match {
    case number Or string => println(s"$number as $string")
  }

  object <::> {
    def unapply[A](arg: Seq[A]): Option[(A, Seq[A])] =
      if (arg.isEmpty) None else Some(arg.head, arg.tail)
  }

  val list = List(1, 2, 3)
  list match {
    case head <::> tail => println(s"a list with $head and $tail")
  }

}
