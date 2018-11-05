package examples.reflections

object ReflectionsApp extends App {
  //reflections + macros + quasiquotes

  case class Person(name: String) {
    def sayMyName() = println(s"my name is $name")
  }

  import scala.reflect.runtime.{universe => ru}

  //Define a mirror. Mirror reflects a type

  val m = ru.runtimeMirror(getClass.getClassLoader) //create a mirror using the default class loader

  //Create a class object by name
  //the return type is ClassSymbol (Class description), reflection library has different symbols as well like MethodSymbol, TypeSymbol, ...
  val clazz: ru.ClassSymbol =
    m.staticClass("examples.reflections.ReflectionsApp.Person")

  //Create a reflected mirror (can do things)
  val cm = m.reflectClass(clazz)

  //get the constructor
  val constructor: ru.MethodSymbol = clazz.primaryConstructor.asMethod

  //reflect the constructor
  val constructorMirror = cm.reflectConstructor(constructor)

  //invoke the constructor
  val instance = constructorMirror.apply("Mohsen")

  println(instance)

  //I have an instance and want to call a method on the instance by name
  val p = Person("Mohsen")
  val methodName = "sayMyName"
  //Reflect the instance
  val reflected = m.reflect(p)

  //method symbol, using a different way. if you don't have the type use the class mirror for this purpose
  val methodSymbol: ru.MethodSymbol =
    ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod

  //reflect the method
  val method = reflected.reflectMethod(methodSymbol)

  //invoke
  method.apply()


  //type erasure
}
