<head>
   <title>TAF Asserts (Deprecated)</title>
</head>

# TAF Asserts (Deprecated)

There are many different classes of assertion methods available to TAF users, for example: TestNG, JUnit & TAF. Not all
of these classes work well with the reporting mechanism in TAF. For that reason we have created a class which contains all the
assertions a user will need which also logs failures to the report properly.

To use these assertions in your test code just statically import the assertions class as follows:

```java
    import static com.ericsson.cifwk.taf.assertions.TafAsserts.*;
```

Your test case will now have direct use of all the TAF assert methods.

## Assertion Types Available

A wide range of assertion methods are now available through the TafAsserts class. Below is a list of the most popular and useful assertion methods:

* **assertEquals(&lt;type&gt; actual, &lt;type&gt; expected)** - Compares the actual value passed is equal to the expected value.

* **assertTrue(boolean condition) & assertFalse(boolean condition)** - Evaluates a condition to be either true or false.

* **assertNotEquals(&lt;type&gt; actual, &lt;type&gt; expected)** - Checks that the two values passed are not equal to each other.

* **assertNull(Object o) & assertNotNull(Object o)** - Verifies an object to be null or not null.

TafAsserts also includes assertion methods for Collections and arrays. Useful methods such as:

* **assertEqualsInOrder()** - Checks that the elements in a collection/array of the same size are identical.

* **assertEqualsNoOrder()** - Checks that a collection/array of the same size have the same elements values but without any order.

The assertion methods mentioned above are only a few of the assertion methods available.

All the information for every assertion method available in the new class can be found in the Javadoc’s, class name is TafAsserts.

## SaveAsserts

In the TafAsserts class we have methods which will continue running a test suite if the assertion fails. The failure is logged
and the result is saved for evaluation on the end of the test suite, here are some examples.

```java
    import static com.ericsson.cifwk.taf.assertions.TafAsserts.*;

    public class SaveAssertExample extends TorTestCaseHelper {

      @Test
      public void saveAssertEquality(){

        List list1 = new ArrayList();
        list1.add("item1");
        list1.add("item2");

        List list2 = new ArrayList();
        list2.add("item2");
        list2.add("item1");

        saveAssertEquals(list1, list2);

        saveAssertEqualsOrderNotImportant(list1, list2);

      }

      @Test
      public void saveAssertObjects(){

        saveAssertOjectNotNull(new Integer(1));

        saveAssertOjectIsNull(null);
      }
    }
```
