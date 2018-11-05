package examples.validation
import scalaz._
import Scalaz._

object ValidationApp extends App {
  final case class Credentials(user: Username, name: Fullname)
  final case class Username(value: String) extends AnyVal
  final case class Fullname(value: String) extends AnyVal

  //using disjunction
  //Disjunction is used to stop at the first failure
  def username(in: String): String \/ Username =
    if (in.isEmpty) "empty username".left
    else if (in.contains(" ")) "username contains spaces".left
    else Username(in).right

  def realname(in: String): String \/ Fullname =
    if (in.isEmpty) "empty real name".left
    else Fullname(in).right

  val result = for {
    u <- username("sam halliday")
    r <- realname("")
  } yield Credentials(u, r)

  //or
  val result2 = (username("sam halliday") |@| realname(""))(Credentials.apply)
  println(result2)

  //using Validation
  //Validation reports all failures
  def username2(in: String): ValidationNel[String, Username] =
    if (in.isEmpty) "empty username".failureNel
    else if (in.contains(" ")) "username contains spaces".failureNel
    else Username(in).success

  def realname2(in: String): ValidationNel[String, Fullname] =
    if (in.isEmpty) "empty real name".failureNel
    else Fullname(in).success

  val result3 = (username2("sam halliday") |@| realname2(""))(Credentials.apply)
  println(result3)
}
