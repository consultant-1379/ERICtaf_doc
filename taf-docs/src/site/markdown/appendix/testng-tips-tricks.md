<head>
   <title>TestNG Tips & Tricks</title>
</head>

# TestNG Tips & Tricks

## What is TestNG

* TestNG is a testing framework.

* Designed to simplify a broad range of testing needs.

* Unit to Integration testing.

* Uses annotations and xml.

* All information at [TestNG.org](http://testng.org/doc/documentation-main.html)

## Benefits

* Annotations.

    * @Test. Annotate a method with this and it is then a test.
    * And many more…

* Data Driven Testing

    * Using the @DataProvider annotation you can set up your test case to take test data as input.
    * Add more and more data and you never need to update your test case.

```java
@Test(dataProvider="calculatorData")
void testSimpleCalculatorAPI(Double firstOperand, Double secondOperand, String operator,
Double result) {
    assertEquals(SimpleCalculatorAPI.calculate("$firstOperand $operator $secondOperand"), result);
}

@DataProvider(name="calculatorData")
public static Object[][] dataForCalculatorTest() {
    /* for every array of data the test is run another time. */
    Object[][] data = {
        {10, 10, "add", 20},
        {15, 5, "subtract", 10},
        {5, 7, "multiply", 35}
    };
    return data;
}
```java

## Basics

* Installing Eclipse plugin.

    * [Eclipse plugin download site](http://testng.org/doc/download.html)

* Maven Dependency.

    * Through a transitive dependency in TAF you already have TestNG.

* Running a test in Eclipse.

    * Individual test
        * right click on the class in the package explorer -> Run As -> TestNG Test

    * Suite
        * right click on the xml file in the package explorer -> Run As -> TestNG Suite

    * Starting listeners.
        * There are several interfaces that allow you to modify TestNG’s behavior. These interfaces are broadly called ``TestNG Listeners''.
          Example: TorNgSuiteListener -> Starts our monitors, loads all TAF Annotations into memory and puts the graph jpegs into the html report
          afterwards.
        * These listeners can be specified in the suite.xml
        * ServiceLoader
            * This is a different way of loading the listeners. Package the listeners into a jar and put that on the class path. Will enable company
              wide use of the same listeners without any developer having to remember where the listeners are stored, just put the jar on the classpath.

* Running the test as part of the maven build.

    * If you create your project from the TAF archetype it is already configured to run the test as part of a Maven install.

## Features

* Data Providers

    * Above I’ve detailed the advantage of using the Data Provider.

    * What is the difference in returning Object[][] and Iterator&lt;Object[]&gt; from the DataProvider method?
        * Use Object[][] when your data is static, use Iterator&lt;Object[]&gt; when you’re data may change during the course of testing, you may be adding
          and deleting users.

    * Why TAF is using DataProvider and TestData
        * You may have wondered why in the TAF structure there is TestDataProvider and Data Provider. – We have these 2 classes so that
          the TestDataProvider can take the data from the DataProvider and supply it to the test case and the DataProvider can also provide the data to
          the operator so that we can get the correct values for the expected and actual and compare them in the testcase. The goal of the
          TestDataProvider is just to stream the data to the test case.

* Before/After(Method,Class,Suite)

    * Before/After Method/Suite/Class annotations allow you to execute a method before/after a method/suite/class. Can be
      useful for preparing something to enable the test.

    * A simple example is in our AbstractMonitorTest, before the suite starts we sleep for 5 seconds.

    * There is an issue with JCAT and using the @BeforeSuite and @AfterSuite annotation, when you pass multiple suite.xml files jenkins
      and the html report shows a failed configuration.

* Groups

    * Groups can be used to organize how and when you run tests.

    * A test can have multiple groups and in the suite.xml file you can specify the groups you can to run or exclude.

    * Example code in the next section.

    * Groups will also be used by RV when they want to run all tests from all teams.

## Extras

* Verbose

    * Increase the level of TestNG logging which is shown in the console.

    * This may be handy if there is an issue in your tests and all TestNG is showing is a one line exception.

* Including/excluding groups

* Running in parallel.

    * Method, class and suite level.

```
<!-- VERBOSE: include it in the suite tag -->
<!-- PARALLEL: include parallel and thread-count in the suite tag -->
<suite name="RemoteFileHandlerSuite" verbose="10" parallel="methods" thread-count="5">
    <!-- This is a comment in the suite.xml file -->

    <test name="RemoteFileHandlerSuite">
<!-- GROUPS: In the groups tag you can include/exclude groups of testcases to be run -->
        <groups>
            <run>
                <exclude name="Regression" />
            </run>
        </groups>
        <classes>
            <class name="com.ericsson.cifwk.taf.taffit.test.cases.RemoteFileHandler" />
        </classes>
    </test>
```
