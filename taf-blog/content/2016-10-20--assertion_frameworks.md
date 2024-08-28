Title: So many assertion libraries, so little time!
Slug: so-many-assertion-libraries-so-little-time
Date: 2016-10-26 12:11
Category: Open Source
Tags: open source, recommendations
Authors: Thomas Melville
Status: published
Summary: There are many assertion libraries out there, in this blog post I share my thoughts on the pros and cons of 6 libraries.

My original intention when I sat down to write this blog was to recommend different libraries for different use cases.
However, as I worked through it I came to the conclusion that [AssertJ](http://joel-costigliola.github.io/assertj/) is the assertion library I recommend for all acceptance tests. 

Currently there are 2 assertion libraries that I like:

* [AssertJ](http://joel-costigliola.github.io/assertj/)
* [Google Truth](http://google.github.io/truth/)

One that I can take or leave

* [Hamcrest](https://github.com/hamcrest/JavaHamcrest)

And 3 I wouldn't use

* [TAF Assert](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#_taf_asserts)
* [TestNG](http://testng.org/javadoc/org/testng/Assert.html)
* [Junit](http://junit.org/apidocs/org/junit/Assert.html)

Why, you ask. Let's start from the bottom up and I'll explain:

**Junit**: For the simple reason that it does not give much information in the Allure Test Report when an assertion fails.

**TAF Asserts**: These are deprecated as all they do is wrap Hamcrest asserts, just use Hamcrest asserts instead.

**TestNG**: the API isn't fluent which makes it easy to mix up actual and expected, and not so easy to find matchers.

**Hamcrest**: Custom matchers are easy enough to implement but the API isn't fluent which makes it not so easy to find matchers.

**Google Truth**: The API is fluent so when you specify your actual value you get all the possible assertions presented to you.

**AssertJ**: This is also a fluent API, with the addition of 2 advantages. It is possible to chain assertion method calls and custom matchers are easy enough to implement.


Let's take some use cases and see why I recommend **AssertJ**

#### Verification of primitive and String values

```java
    //...
    
    import static org.assertj.core.api.Assertions.assertThat;
    
    //...
    
    // ints
    assertThat(expectedIntValue).isGreaterThan(5).isLessThan(20);
    
    // Strings
    assertThat(expectedValue).startsWith("This is").contains("the").endsWith("end");
    
```

The Fluent API makes it easy to see all the possible assertions you can do on the value, and you can chain them to reduce lines of code.

#### Verification of contents of a collection

```java
    //...
    
    import static org.assertj.core.api.Assertions.assertThat;
    
    // Lists
    assertThat(results).contains("Help").hasSize(4);
    
    // Maps
    assertThat(filterMap).containsEntry("key", "value");
```

The assertion methods for collections are powerful and reduce the logic in your tests.

#### Verification of state of custom objects

Here is our custom assertion class.

```java
    import org.assertj.core.api.AbstractAssert;
    
    public class CommandResponseAssert extends AbstractAssert<CommandResponseAssert, CommandResponse> {
    
        public CommandResponseAssert(CommandResponse actual){
            super(actual, CommandResponseAssert.class);
        }
    
        public static CommandResponseAssert assertThat(CommandResponse actual){
            return new CommandResponseAssert(actual);
        }
    
        public CommandResponseAssert hasErrorCode(int expectedErrorCode){
            isNotNull();
            int actualErrorCode = actual.getSummaryDto().getErrorCode();
            if(actualErrorCode != expectedErrorCode){
                failWithMessage("Expected error code to be <%d> but was <%d>", expectedErrorCode, actualErrorCode);
            }
            return this;
        }
    }
```

A few lines of boiler plate code but once that's done you can add as many assertion methods for this object as you like.
There is a great step by step guide on the [AssertJ Website](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html) for implementing your own custom assertions.

Here is an example usage of the custom assertion class above.

```java
    //...
    
    CommandResponseAssert.assertThat(commandResponse).hasErrorCode(0);
    

```

I hope this blog post has been helpful to you, and the next time you import an assertion library you'll import [AssertJ](http://joel-costigliola.github.io/assertj/)
