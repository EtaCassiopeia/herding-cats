package examples.variance

object ContravarianceApp extends App {
  trait Event

  trait UserEvent extends Event

  trait SystemEvent extends Event

  trait ApplicationEvent extends SystemEvent

  trait ErrorEvent extends ApplicationEvent


  trait Sink[-In] {
    def notify(o: In)
  }

  def appEventFired(e: ApplicationEvent, s: Sink[ApplicationEvent]): Unit = {
    // do some processing related to the event
    // notify the event sink
    s.notify(e)
  }

  def errorEventFired(e: ErrorEvent, s: Sink[ErrorEvent]): Unit = {
    // do some processing related to the event
    // notify the event sink
    s.notify(e)
  }

  trait SystemEventSink extends Sink[SystemEvent]

  val ses = new SystemEventSink {
    override def notify(o: SystemEvent): Unit = ???
  }

  trait GenericEventSink extends Sink[Event]

  val ges = new GenericEventSink {
    override def notify(o: Event): Unit = ???
  }


  appEventFired(new ApplicationEvent {}, ses)

  errorEventFired(new ErrorEvent {}, ges)

  appEventFired(new ApplicationEvent {}, ges)
}
