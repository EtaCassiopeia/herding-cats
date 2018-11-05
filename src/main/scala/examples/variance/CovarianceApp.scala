package examples.variance

object CovarianceApp extends App {

  trait Event

  trait UserEvent extends Event

  trait SystemEvent extends Event

  trait ApplicationEvent extends SystemEvent

  trait ErrorEvent extends ApplicationEvent

  trait Source[+Out] {
    def get(): Out
  }

  trait UserEventSource extends Source[UserEvent]

  val ues = new UserEventSource {
    override def get(): UserEvent = ???
  }

  trait SystemEventSource extends Source[SystemEvent]

  val syes = new SystemEventSource {
    override def get(): SystemEvent = ???
  }

  def forwardEventsComingFrom(source: Source[Event]): Unit = {
    // imagine events are continuously intercepted as they arrive
    source.get()
    // and then forwarded
  }

  forwardEventsComingFrom(ues)

  forwardEventsComingFrom(syes)

}
