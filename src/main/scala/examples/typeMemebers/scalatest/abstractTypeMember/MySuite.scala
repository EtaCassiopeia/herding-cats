package examples.typeMemebers.scalatest.abstractTypeMember

//The problem was that I wanted to provide traits in ScalaTest that allow users to write tests into which they can pass fixture objects.
// Type member version
class MySuite extends FixtureSuite with StringBuilderFixture {

}
