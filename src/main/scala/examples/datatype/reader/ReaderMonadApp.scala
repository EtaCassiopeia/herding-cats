package examples.datatype.reader

import examples._
object ReaderMonadApp extends App {
  //The Reader Monad is a monad defined for unary(In Scala, a unary function (a function with one parameter) is an object of type Function1) functions,
  //using andThen as the map operation. A Reader, then, is just a Function1.

  //-Composition
  import cats.data.Reader
  val upper = Reader((text: String) => text.toUpperCase)
  val greet = Reader((name: String) => s"Hello $name")

  val comb1 = upper.compose(greet)
  val comb2 = upper.andThen(greet)
  val result1 = comb1.run("Bob")
  println(result1) // prints Hello Bob

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

  def isAuthorised: Reader[CourseManager, Boolean] =
    Reader[CourseManager, Boolean] { courseMgr =>
      courseMgr.authService.isAuthorised(courseMgr.userName)
    }

  def register(isFull: Boolean): Reader[CourseManager, String] =
    Reader[CourseManager, String] { courseMgr =>
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

  report.print()


  /*
  def getUser(id: Int) = Reader((userRepository: UserRepository) =>
    userRepository.get(id)
  )

  run(getUser(id))

  private def run[A](reader: Reader[UserRepository, A]): JsValue = {
    Json.toJson(reader(userRepository))
  }
   */

}
