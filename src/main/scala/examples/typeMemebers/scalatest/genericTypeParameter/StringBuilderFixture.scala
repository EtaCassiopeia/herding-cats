package examples.typeMemebers.scalatest.genericTypeParameter

//However, one other use case I had is that I wanted to allow people to create traits that provide a concrete definition for the fixture type and could be mixed into suite classes. This would allow users to encode commonly used fixtures into helper traits that could be mixed into any of their suite classes that need them.

// Type parameter version

//The "this: FixtureSuite[StringBuilder]" syntax is a self type, which indicates that trait StringBuilderFixture can only be mixed into a FixtureSuite[StringBuilder].
trait StringBuilderFixture { this: FixtureSuite[StringBuilder] =>

}
