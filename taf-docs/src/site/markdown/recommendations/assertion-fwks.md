<head>
   <title>What Assertion Framework Should I use</title>
</head>

# What Assertion Framework Should I use

Junit Assert, TestNg Assert, Hamcrest Assert, Taf Assert, Google Truth Assert, ...who knows what the future holds

Which one is the best? Is there one that is the best?

The TAF team don't enforce the usage of any one particular assertion framework, but we do have an order of recommendation.

Assertion Frameworks recommended by TAF:

* [AssertJ](http://joel-costigliola.github.io/assertj/)

    * IDE friendly

    * Fluent API

    * Chained assertions

    * Both Java 7 and Java 8 versions exist

* [Google Truth](http://google.github.io/truth/)

    * Fluent API

* [Hamcrest](https://github.com/hamcrest/JavaHamcrest)

    * Easy to write custom matcher methods

Assertion Frameworks **not** recommended by TAF:

* [TAF Assert](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#_taf_asserts)

    * There are better open source alternatives available

    * It will be deprecated in future

* [TestNG](http://testng.org/javadoc/org/testng/Assert.html)

    * This API just wraps Hamcrest and will be deprecated

* [Junit](http://junit.org/apidocs/org/junit/Assert.html)

    * Doesn’t give much information in the Test Report
