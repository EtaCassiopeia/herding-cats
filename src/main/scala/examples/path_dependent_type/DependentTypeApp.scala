package examples.path_dependent_type

object DependentTypeApp {

  trait DepValue {
    type V
    val value: V
  }

  def magic(that: DepValue): that.V = that.value

  def mk[T](x: T) = new DepValue {
    override type V = T
    override val value: V = x
  }

  val depInt = mk(1)
  val depString = mk("Scala")

  val intValue: Int = magic(depInt)
  val stringValue: String = magic(depString)

  //using Type parameter
  trait TypedValue[T] {
    val value: T
  }

  def mk2[T](x: T) = new TypedValue[T] {
    override val value: T = x
  }

  def magic2[T](that: TypedValue[T]):T = that.value

  val typedInt = mk2(1)
  val typedString = mk2("Scala")

  val intValue2: Int = magic2(typedInt)
  val stringValue2: String = magic2(typedString)

}
