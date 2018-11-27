package examples.reader_monad

import scalaz._
import Scalaz._

object ReaderMonadApp extends App {
  /*
A Reader, sometimes called the environment monad, treats functions as values in a context.
Loosely speaking, it allows you to build a computation that is a function of some context (configuration, session, database connection, etc.), rather than passing the context as an argument to the function.

Reader monad is a wrapper on top of A => B and usually we can use it to composition and dependency injection.
   */

  //composition
  val upper = Reader((text: String) => text.toUpperCase)
  val greet = Reader((name: String) => s"Hello $name")

  println(upper.compose(greet).run("Bob"))

  //Dependency Injection
  case class Course(desc: String, code: String)

  class AuthService {
    def isAuthorised(userName: String): Boolean = userName.startsWith("J")
  }

  class CourseService {
    def register(course: Course, isAuthorised: Boolean, name: String) = {
      if (isAuthorised)
        s"User $name registered for the course: ${course.code}"
      else
        s"User: $name is not authorised to register for course: ${course.code}"
    }
  }

  case class CourseManager(course: Course,
                           userName: String,
                           authService: AuthService,
                           courseService: CourseService)

  def isAuthorised = Reader[CourseManager, Boolean] { courseMgr =>
    courseMgr.authService.isAuthorised(courseMgr.userName)
  }

  def register(isFull: Boolean) = Reader[CourseManager, String] { courseMgr =>
    courseMgr.courseService.register(courseMgr.course,
                                     isFull,
                                     courseMgr.userName)
  }

  val result = for {
    authorised <- isAuthorised
    response <- register(authorised)
  } yield response

  val course = Course("Computer Science", "CS01")
  val report =
    result.run(CourseManager(course, "Jon", new AuthService, new CourseService))
  println(report) // prints: User Jon registered for the course: CS01

  /*
   object Reader extends scala.Serializable {
    def apply[E, A](f: E => A): Reader[E, A] = Kleisli[Id, E, A](f)
  }
   */

  /*
    type Reader[E, A] = ReaderT[Id, E, A]
   */

  case class Config(host: String, port: Int)

  val host = Reader((c: Config) => c.host)
  val port = Reader((c: Config) => c.port)

  val hostAndPort = for {
    h <- host
    p <- port
  } yield (h, p)

  println(hostAndPort(Config("localhost", 9090)))
}
