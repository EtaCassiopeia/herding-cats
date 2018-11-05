package examples.typeMemebers.scalatest.genericTypeParameter

//The problem was that I wanted to provide traits in ScalaTest that allow users to write tests into which they can pass fixture objects.
// Type parameter version
class MySuite extends FixtureSuite[StringBuilder] with StringBuilderFixture { //In the type parameter approach, the user must repeat the type parameter even though it is defined in the trait:

}
