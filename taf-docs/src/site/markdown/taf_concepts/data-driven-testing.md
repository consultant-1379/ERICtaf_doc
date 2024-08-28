<head>
   <title>What is DDT?</title>
</head>

# What is DDT?

TAF supports what is called the data-driven testing concept (http://en.wikipedia.org/wiki/Data-driven_testing).
In a nutshell it means that the same test case gets executed N times, where N is the number of lines in a CSV file,
number of records in a DB table or any other source of data. Data-driven testing helps separate test data from test
logic and makes your tests more maintainable.

## How does TAF support DDT?

TAF integrates with TestNG to provide several new annotations (@DataDriven, @DataProviders, @Input, @Output and @TestId).

Currently TAF supports the following data source types:

* Comma-Separated Values (CSV)

* Custom Java class

The linking of the annotations in the code and the data is done through a properties file:

```
# Loading data from csv
dataprovider.calculator.type=csv
dataprovider.calculator.location=calculator.csv
dataprovider.calculator.strategy=stop_on_end

# Loading data from custom Java class
dataprovider.from_java_class.type=class
dataprovider.from_java_class.class=com.ericsson.cifwk.taf.extension.CustomDataSource

# Loading external csv file via its uri
dataprovider.by_uri.type=csv
dataprovider.by_uri.uri=http://myserver.com:8080/data/calculator.csv
```

The value "calculator" is used in the @DataDriven annotation to tell the test method to get the data from the file
specified in the location parameter. @TestId annotation additionally to @Input allows taking Test ID from data
source (instead of hard-coding it in the test method), please look DataDrivenTest example below.

**TIP** | *It is possible to supply fully qualified URI as reference to data file. All Java standard protocols are supported.*

## CSV

```
user,password,result
exyzzyx,password,OK
root,hello,FAIL
exyzzyx,wrong,FAIL
```

The csv values above represent the Inputs and Outputs provided to your test case method that implements @DataDriven
annotation. The first line in the csv will always define the header details for the subsequent lines of data to follow.
Taking the above as an example: user, password and result are the column headers and so, every line of data following
will be grouped under each of these individual headings. E.g. user=root, password=hello, result=FAIL

```java
package datadriven;

import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import org.testng.annotations.Test;

public class DataDrivenTest {

    @Test
    @TestId(id = "TAF_Scenarios_0092")
    @DataDriven(name = "calculator")
    public void dataDriven(@Input("x") int x, @Input("y") int y, @Output("z") int z) {
        // test code
    }

    @Test
    @DataDriven(name = "calculator")
    public void dataDrivenWithTestId(@Input("x") int x, @TestId @Input("y") String testId,
    @Output("z") int z) {
        // test code
    }

}
```

### Complex CSV Example

What if you want to pass in an array of data to a single variable in your test? Or if your data is a string and
contains a comma?

```
list,option,text
"This,is,a,list",5,"This is some text, I like it"
```

```java
package datadriven;

import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import org.testng.annotations.Test;

public class ComplexDataDrivenTest {

    @Test
    @DataDriven(name = "calculator")
    public void dataDrivenTest(
                @Input("list") String[] list,
                    @Input("option") int option,
                    @Input("text") String text) {
        // test code
    }
}
```

### Using a custom CSV delimiter

It is possible to change the delimiter used in your csv data files. This can be done at a project level and at a
data source level.

#### Project Level

At a project level a custom delimiter can be used by specifying a property in your taf_properties.

```
taf.csv.delimiter=:
```

Your data file would then need to match the delimiter as follows:

```
user:password:result
exyzzyx:password:OK
root:hello:FAIL
exyzzyx:wrong:FAIL
```

#### DataSource Level

Each dataSource can have its own delimiter by specifying it in the datadriven.properties file.

```
dataprovider.mydataprovider.type=csv
dataprovider.mydataprovider.location=data.csv
dataprovider.mydataprovider.delimiter=|
```

### Commenting out lines of csv

There may be times when you want to reduce the number of rows used in your testing. Instead of deleting those rows you can "comment them out"

By default the comment identifier is #, so just insert at the start of the row and this row will not be used during test execution.

You can configure this using the following property: taf.data.comment.identifier

### Property replacement in data

It is now possible to embed property keys in data which will be replaced at runtime with the property values.

emails.csv
```text
recipient,sender,subject,body
user.a@email.com,${sender},hello,hi there
user.b@email.com,${sender},hello,hi there
user.c@email.com,${sender},hello,hi there
user.d@email.com,${sender},hello,hi there
```

```properties
sender=me@mine.com
```

**Note:** For a more in depth example please see the following blog post: https://taf.seli.wh.rnd.internal.ericsson.com/
.se/blog/embedded-properties-in-data.html

**Please note the following**

1. This works with the following types:

* primitives
* String
* Array/List
* Map<?, String> Where the property can't be in the key, only the value
* DataRecord
* DataRecord extensions

2. When using a DataRecord extension, the substitution is done when the get method is called, so the Scenario Logging 
will show the key and not the value.

Example:

```text
2017-10-27 10:20:22,703 [Set PIB Parameters in VNF LAF.Set PIB Parameters flow.vUser-1] [INFO] [com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener] VUser: 1 Starting test step : VnfLafTestSteps.setPibParameter(Data value: {paramName=ossType, paramValue=${scripting.osstype}})
```

## Custom Java Class

In case you need to have more control of the test data it is possible to create a custom Java class, which would
provide value combinations. The following example demonstrates the simplest case of such usage.

```java
package datadriven;

import com.ericsson.cifwk.taf.annotations.DataSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomDataSource {

    // Result should implement java.lang.Iterable<Map<String,Object>>
    @DataSource
    public List<Map<String,Object>> dataSource() {
        Map<String, Object> data = Collections.<String, Object>singletonMap("Pi", "3.14");
        return Collections.singletonList(data);
    }

}
```

This class should have no-arg constructor and one method annotated with @DataSource annotation. This method should
return Iterable<Map<String,Object>> or any subclass, such as List. If you compare this data structure to CSV files,
then every collection item would correspond to one line of CSV file. And every map inside will correspond to CSV
line values. In this case key of the map element is CSV column header name and value is actual value.

It is possible to set the following settings on data source level. They are set in the form dataprovider.&lt;name&gt;.attribute
where &lt;name&gt; is the name of the data source referenced from your test.

Name      | Values                            | Default     | Description
:-------  | :--------------                   | :-------    | :--------------
type      | class, csv                        |             | Type of the data source.
location  | path                              |             | Location of data source files on the disk.
uri       | uri                               |             | Remote URI of data source.
strategy  | stop_on_end, repeat_until_stopped | stop_on_end | Flow logic of the data source records.
usage     | shared, copied                    | copied      | Marks if data source is shared or copied between vusers.
class     | fully qualified class name        |             | Reference to class annotated with @DataSource.

## Multiple data sources

Since TAF 2.2.9 it’s possible to use multiple data sources in one test. To do this, you need to annotate your
test method with <span style="color:#ba3925;">@DataProviders</span> annotation, and put 1 or more <span style="color:#ba3925;">@DataDriven</span> annotations inside.

Example:

```java
@DataProviders ({
     @DataDriven(name="a"),
     @DataDriven(name="b")
 })
@Test
public test(@Input("a.foo") String foo, @Input("a.bar") String bar, @Input("b.buzz") String buzz) {
    ....
}
```

In this case the <span style="color:#ba3925;">@Input</span> annotations of your parameters have to have <span style="color:#ba3925;">&lt;dataSourceName&gt;.&lt;parameterName&gt;</span> notation, where
<span style="color:#ba3925;">dataSourceName</span> is the data source name from<span style="color:#ba3925;">@DataDriven</span> annotation, and <span style="color:#ba3925;">parameterName</span> is your parameter name.

--------------  |  --------------
  **NOTE**      |  *You can omit the definition the data source prefix (like in case of a test with single data source), and all the field values will still be injected into method parameters, but there’s a high risk here: in this case fields with the same name from different data sources will map to the same test method parameter and you can get unpredictable results. This approach should be avoided.*

### How many times the test will be called in case of multiple data sources?

In case of multiple data sources the test is executed min(<span style="color:#ba3925;">dataSource1.size, dataSource2.size, …</span>) times.

Even if one of the data sources has strategy <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span>, test execution amount will be equal to the
amount of the rows in the smallest data source. If you use multiple data sources, and want your test to be executed
until stopped, all of the used data sources have to have the <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> strategy.

### Example 1: Two data sources with the default (STOP_ON_END) strategy.

<span style="color:#ba3925;">Data source A</span> (class data source)

foo  | bar
:--- | :---
1    |   a
2    |   b

<span style="color:#ba3925;">Data source B</span> (CSV data source)

buzz      | blah
:------   | :------
23.456    | &lt;x&gt;..&lt;x&gt;
-124.2    | &lt;y&gt;..&lt;y&gt;
899.1     | &lt;z&gt;..&lt;z&gt;

<span style="color:#ba3925;">Invocations of test</span> (CSV data source)

foo  | bar  | buzz
:--- | :--- | :-----
1    | a    | 23.456
2    | b    | -124.2

### Example 2: Two data sources with the different data provision strategies.

<span style="color:#ba3925;">Data source A</span> (class data source, <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> (cycles))

foo  | bar
:--- | :---
1    |   a
2    |   b

<span style="color:#ba3925;">Data source B</span> (CSV data source, <span style="color:#ba3925;">STOP_ON_END</span> (empty after the last row of data is read))

buzz      | blah
:------   | :------
23.456    | &lt;x&gt;..&lt;x&gt;
-124.2    | &lt;y&gt;..&lt;y&gt;
899.1     | &lt;z&gt;..&lt;z&gt;

<span style="color:#ba3925;">Invocations of test</span> (CSV data source)

foo  | bar  | buzz
:--- | :--- | :-----
1    | a    | 23.456
2    | b    | -124.2
1    | a    | 899.1

Please note that the last invocation used the first row from data source A - because its data was fully read on iteration
2 (it has only 2 rows), but due to <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> strategy the reader pointer was
reset and on iteration 2 it points to first row again. However, the overall invocation amount was 3, because there are just
3 rows in data source B, and data source B is not a cycling data source.

## Data Record

Since TAF 2.2.9 it’s possible to map all data source row data to a single object - <span style="color:#ba3925;">DataRecord</span> - and
work with this object to get the test parameters. Just use the <span style="color:#ba3925;">DataRecord</span> in your test’s
parameters, and define the data source name in its <span style="color:#ba3925;">@Input</span> annotation. This is very handy
when dealing with a data source that has a lot of parameters, or when dealing with multiple data sources:

```java
    @Test
    @DataDriven(name = "users")
    public void test(@Input("users") DataRecord users) {
        String userName = users.getFieldValue("userName");
        String userHomeAddress = users.getFieldValue("address1");
        String userZipCode = users.getFieldValue("zipCode");
        Long systemId = new Long(users.getFieldValue("systemId").toString());
        UserAccount account = userOperator.persist(systemId, userName, userHomeAddress);
        assertNotNull(account);
        String userType = users.getFieldValue("type");
        account.setUserType(userType);
        assertTrue(account.verifyChanges());
    }

    @DataProviders ({
         @DataDriven(name="users"),
         @DataDriven(name="admins")
     })
    @Test
    public void test2(@Input("users") DataRecord users, @Input("admins") DataRecord admins) {
    .............
    }
```

--------------  |  --------------
  **NOTE**      | *If the data source is a csv file all values in the DataRecord are Strings. Java primitive wrapper classes can be used to wrap the Strings into the desired type.*

### Transformer

Since TAF &lt;VERSION&gt; it’s possible to transform a DataRecord into a JavaBean and vice versa.

### DataRecord to Bean

DataRecord contains a new method <span style="color:#ba3925;">public &lt;T&gt; T transformTo(Class&lt;T&gt; beanClass)</span> It is a
convenient way to create a bean with all the data that is stored inside DataRecord object.

```java
    TransformedBean bean = fromDataRecord.transformTo(TransformedBean.class);
```

### Bean to DataRecord

Class  <span style="color:#ba3925;">BeanTransformer</span> allows to transform Java bean into a DataRecord. Data is transferred
based on getter match, e.g. DataRecord’s method getUser() will be populated by information from bean object visible under getUser() method.

```java
    MyDataRecord mdr = BeanTransformer.transformTo(MyDataRecord.class, bean);
```

### Test Context

Since TAF 2.2.9 it’s possible to share data between test steps using new entity: <span style="color:#ba3925;">TestContext</span>. You can use
it to store attributes or even build up your own data sources that can be referenced in other tests.

There are few ways to get access to <span style="color:#ba3925;">TestContext</span>:

* Inject it into test class using Java DI:

```java
    @Inject
    TestContext context;
```

* Get it from singleton inside the test method: <span style="color:#ba3925;">TestContext context = TafTestContext.getContext();</span>

--------------  |  --------------
  **NOTE**      | *Please note that you need to ensure the sequence of test execution if you are passing the values from one test to another using test context. You can do it by defining the sequence in TestNG test suite XML, and adding the <span style="color:#ba3925;">preserve-order</span> directive to test declaration (example: &lt;test name="Manual test run" preserve-order="true"&gt;).*

## Context (user-defined) data source

You can use test context to build and store your own data sources. This is handy when you need to store the data that is not
known before the tests, and is retrieved only during the test execution. Example: you have a list of users in CSV file, and you
need to register them. IDs are generated by the service which you test, and you don’t know them beforehand. You can use the
context (user-defined) data source to keep the IDs so that you can use them in other tests.

```java
    @Test
    @DataDriven(name = "users")
    public void addUsers(@Input("users") DataRecord userToBeAdded) {
       UserAccount userAccount = userOperator.addUser(userToBeAdded.getFieldValue("username"),
                                                      userToBeAdded.getFieldValue("password"));
       if (userAccount != null) {
          testContext.dataSource("addedUsers").addRecord()
            .setField("id", userAccount.getUserId())
            .setField("userType", "tmpGuest");
       }
    ....................
    }

    ....................

    @Test
    @DataDriven("addedUsers")
    public void deleteUsers(@Input("id") Long userId) {
       userOperator.deleteUser(userId);
    }
```

It is possible to add a DataRecord to a dataSource by using setFields(DataRecord record) and update individual properties of
the DataRecord using setField.

```java
    @TestStep(id="AddUser")
        public void addUsers(User userToBeAdded) {
        ....................
        testContext.dataSource("addedUsers").addRecord()
            .setFields(userToBeAdded)
            .setField("username", "admin");
    }
```

It is possible to add an Object to dataSource by using setFields(Object record) and update individual properties of the object using setField.

```java
    @TestStep(id="AddUser")
            public void addUsers(Object userToBeAdded) {
            ....................
            testContext.dataSource("addedUsers").addRecord()
                .setFields(userToBeAdded)
                .setField("username", "admin");
    }
```

## Data filtering

Since TAF 2.2.9 it’s possible to filter the data source rows, controlling the amount of times your test will be executed. Filter expression
is defined in <span style="color:#ba3925;">@DataDriven</span> annotation. Filter expression language is based on MVEL expression language.
You can refer to data source field or test context attribute just defining the field/attribute name in the filter. You can refer to objects,
collections and maps inside the filter. Data filter supports a reserved keyword $VUSER which in runtime evaluates to the ID of the current vUser.

### Example 1: Conditional clauses

```java
    @Test
    @DataDriven(name = "a", filter = "(x > 1 && y != x) || z == 6")
    public void test1(....)
```

### Example 2: Execute command install only for every second vUser

```java
    @Test
    @DataDriven(name = "commands", filter = "$VUSER%2==0 && command=='install'")
    public void test2(....)
```

### Example 3: Filter by a combination of context attribute data

```java
    @Test
    @DataDriven(name = "commands", filter = "myContextMap['userName']=='Sven' &&
    myContextMap['myMapPojo'].myString==myObj.myString")
    public void test3(....)
```

--------------  |  --------------
  **NOTE**      | *Context attributes have a higher priority than data source fields. So if both data source and context has a data entity named "foo", filter will get the context attribute value and ignore the data source field value.*

### How can I make one vUser to execute exactly one command?

It’s easy: define the mapping of vUser IDs and commands in the data source, and put the following filter on your test.

### Example

<span style="color:#ba3925;">Data source contents:</span>

vUser  | cmd
:---   | :---
1      | cp /aa/x.txt /bb/
2      | cp -r /cc/* /dd/
3      | rm -rf ~/*
4      | rm -rf /

<span style="color:#ba3925;">Code that references this data source:</span>

```java
    @Test
    @DataDriven(name = "commands", filter = "$VUSER==vUser")
    public void testVUserCommands(@Input("cmd") String command)
```

## Overriding @DataDriven(name)

In TAF it is possible to override the data all tests use by passing a parameter in the suite.xml file [Parameters]. In TAF it is
also possible to map a full row of data to a single object [Data record]. When doing both of these there is no need to annotate
the DataRecord variable with @Input("dataprovidername").

### Example:

```java
    @Test
    @DataDriven(name = "abc")
    public void verifydata(final DataRecord record) {
    ....................
    }
```

The suite file that would override the data-provider with value="xyz" would look something like below:

```
<suite name="OverrideDataPropertyExamples">
    <parameter name="taf.data-provider" value="xyz"></parameter>
       <test name="OverrideDataPropertyExamples">
            <classes>
                 <class name="com.ericsson.cifwk.taf.taffit.examples.OverrideDataPropertyTest">
            </classes>
       </test>
</suite>
```
