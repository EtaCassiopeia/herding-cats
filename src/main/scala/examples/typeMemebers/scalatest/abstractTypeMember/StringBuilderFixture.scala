package examples.typeMemebers.scalatest.abstractTypeMember


//However, one other use case I had is that I wanted to allow people to create traits that provide a concrete definition for the fixture type and could be mixed into suite classes. This would allow users to encode commonly used fixtures into helper traits that could be mixed into any of their suite classes that need them.

// Type member version
trait StringBuilderFixture { this: FixtureSuite =>
  type F = StringBuilder

}
