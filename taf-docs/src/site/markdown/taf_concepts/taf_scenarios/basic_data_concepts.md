<head>
   <title>Scenarios - Basic Data Concepts</title>
</head>

# Basic Data Concepts

## Data Types

Currently TAF supports the following data source types:

* Comma-Separated Values (CSV)
* Custom Java class
* Contextual Data Source

### CSV

```
username,firstName,lastName,email,password
ejohsmi,John,Smith,john.smith@email.com,pass
ejandoe,Jane,Doe,jane.doe@email.com,12345
```

The csv values above represent the Inputs and Outputs provided to your test step method. The first line in the csv will always define the
header details for the subsequent lines of data that follows.
Taking the above as an example: username,firstName,lastName,email and password are the column headers and so, every line of data following
will be grouped under each of these individual headings.
E.g. username = <span style="color:#ba3925;">ejohsmi</span>, firstName = <span style="color:#ba3925;">John</span>, lastName = <span style="color:#ba3925;">Smith</span>, email = "john.smith@email.com", password = <span style="color:#ba3925;">pass</span>.

Here is how this data would be used in a test step

```java
@TestStep(id = "Login")
public void login(@Input("username") String username,
                    @Input("password") String password)
```java

**<span style="color:#ba3925;">Complex CSV Example</span>**

What if you want to pass in an array of data to a single variable in your test?

```
recipient, subject, body
"ejohsmi,ejandoe",Hello, "Just a mail to say hello"
```

Here is how this data would be used in a test step

```java
@TestStep(id = "composeEmail")
public void login(@Input("recipient") String [] recipients,
                    @Input("subject") String subject,
                    @Input("body") String body)
```java

**<span style="color:#ba3925;">Using a custom CSV delimiter</span>**

It is possible to change the delimiter used in your csv data files. This can be done at a project level and at a data source level.

**Project Level**

At a project level a custom delimiter can be used by specifying a property in a properties file in taf_properties folder in your project.
For example changing the delimiter from "," to ":".

```
taf.csv.delimiter=:
```

Your data file would then need to match the delimiter as follows:

```
username:firstName:lastName:email:password
ejohsmi:John:Smith:john.smith@email.com:pass
ejandoe:Jane:Doe:jane.doe@email.com:12345
```

**DataSource level**

Each dataSource can have its own delimiter by specifying it in the datadriven.properties file.

```
dataprovider.users.type=csv
dataprovider.users.location=users.csv
dataprovider.users.delimiter=:
```

### Custom Java Class

In case you need to have more control of the test data it is possible to create a Java class, which would provide test data. The following
example demonstrates the simplest case of such usage.

```java
package datadriven;

import com.ericsson.cifwk.taf.annotations.DataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDataSource {

    @DataSource
    public List<Map<String,Object>> users() {
        List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();

        Map<String, Object> user = new HashMap<String, Object>();

            user.put("username","ejohsmi");
            user.put("firstName","John");
            user.put("lastName","Smith");
            user.put("email","john.smith@email.com");
            user.put("password","pass");

        users.add(user);
        return users;
    }
}
```java

This class should have no-arg constructor and one method annotated with @DataSource annotation. This method should return <span style="color:#ba3925;">Iterable&lt;Map&lt;String,Object&gt;&gt;</span>
or any subclass, such as List. If you compare this data structure to CSV files, then each list item would correspond to one row of data in the CSV file
and the map inside will correspond to CSV row values. In this case the key of the map element is the CSV column header name and value is actual value.

### Contextual Data Source

TAF provides the option to share data between test steps using a new entity: <span style="color:#ba3925;">TestContext</span>. You can use it to store attributes or even
build up your own data sources that can be referenced in other tests.

:-------  | :--------------
**NOTE:** | TestContext is only shared within the scope of one Vuser

To get an instance of <span style="color:#ba3925;">TestContext</span> inject it into test class or operator using Java Dependency Injection:
More information on dependency injection is available here: [Dependency Injection in TAF](../dependency-injection.html)

```java
@Inject
TestContext context;
```java

**<span style="color:#ba3925;">Adding attributes to the test context</span>**

```java
@Inject
TestContext context;

@TestStep(id = "Login")
    public void login(@Input("user") String user,
                      @Input("pass") String pass) {
          context.setAttribute("username", "user");
          ............
    }

@TestStep(id = "Logout")
    public void logout() {
          log.info("Logging out with user : "
                    + context.getAttribute("username"));
          ............
    }
```java

:-------  | :----------------------------
**NOTE:** | In TAF a TestContext is different from Context. TestContext allows you to share data between test steps and create your own data sources Context is a way to implement business logic and interact with the system under test. There are four contexts, these are REST, API, CLI and UI. Contexts and TestContexts are designed to be able to work together, for example you can run a test with a REST context that shares data between test steps in a TestContext. More information on Contexts and how they are used in TAF is available here: [TAF Context](../taf-contexts-and-operators.html)

**<span style="color:#ba3925;">Global</span>**

If you want data to be usable by all flows you need specify it globally in context.

Here is an example of how.

```java
@Inject
TestContext context;
public static final String USERS = "users";

@BeforeClass
public void setUp(){
    context.dataSource(USERS, User.class);
}
```java

**<span style="color:#ba3925;">Adding Test Data Source to Context</span>**

Data source from CSV file or class can be added to context using convenience methods.

```java
TestDataSource readFromCsv = TafDataSources.fromCsv("data/mailsToSend.csv");
context.addDataSource("mailsToSend", readFromCsv);

TestDataSource getFromClass = TafDataSources.fromClass(Users.class);
context.addDataSource("users", getFromClass);

TestDataSource getFromDataProvider = TafDataSources.fromTafDataProvider("dataProviderName");
context.addDataSource("users", getFromClass);
```java

:-------- | :----------
**NOTE:** | when getting data source by using <span style="color:#ba3925;">fromTafDataProvider</span> method you can not get data source with usage shared. To get shared data source use <span style="color:#ba3925;">shared(fromTafDataProvider("dataProviderName"))</span>.

## DataSource

You can use the test context to build and store your own data sources. This is beneficial when you need to store data that is not known before the tests but is
retrieved later in the test execution.
Example: You have a list of users in CSV file, and you need to register them. IDs are generated by the service which registers the users so you don't know these
IDs beforehand. You can use a user-defined data source, stored in the context to keep the IDs so that you can use them later in other tests.

It is possible to add a DataRecord to a dataSource by using <span style="color:#ba3925;">setFields(DataRecord record)</span> and update individual properties of the object using <span style="color:#ba3925;">setField</span>.

Here is an example of how it would be done.

```java
@TestStep(id="AddUser")
public void addUsers(User userToBeAdded) {
....................
testContext.dataSource("addedUsers").addRecord()
    .setFields(userToBeAdded)
    .setField("username", "admin");
}
```java

### DataSource Configuration

It is possible to set the following settings on data source level. They are set in datadriven.properties file and have the form <span style="color:#ba3925;">dataprovider.&lt;name&gt;.attribute</span> where <span style="color:#ba3925;">&lt;name&gt;</span> is the
name of the data source referenced from your test.

**CSV DataSource Specification**

Name     | Values | Description
:------- | :----- | :------------------
type     | csv    | Type of the data source.
location | path   | Location of data source files in the project.
uri      | uri    | Remote URI of data source.

:-------  | :----------
**NOTE:** | Only one of location or uri needs to be specified

**Class DataSource Specification**

Name  | Values                     | Description
:---- | :-----------------------   | :---------------
type  | class                      | Type of the data source.
class | fully qualified class name | Reference to class annotated with @DataSource.


**DataSource Behaviour Management**

Name      | Values                            | Default     | Description
:-------- | :--------------------------       | :---------- | :-----------------
strategy  | stop_on_end, repeat_until_stopped | stop_on_end | Flow logic of the data source records.<br/> **stop_on_end** - stop execution once all data has been used.<br/> **repeat_until_stopped** - until stop method is called or until other data source is finished when using multiple data sources.
usage     | shared, copied                    | copied      | Marks if data source is shared or copied between Vusers. <br/> **copied** - each Vuser will get complete data source, which is a copy of original data source. <br/> **shared** - one data source is shared between all Vusers, so one Vuser will get one row of data and another Vuser will get a different row of data

Properties for data from csv file

```
dataprovider.users.type = csv
dataprovider.users.location = users.csv
dataprovider.users.strategy = stop_on_end
dataprovider.users.usage = copied
```

```
dataprovider.users.type = csv
dataprovider.users.uri = http://myserver.com:8080/data/users.csv
dataprovider.users.strategy = stop_on_end
dataprovider.users.usage = copied
```

Properties for data from java class

```
dataprovider.users.type = class
dataprovider.users.class = datadriven.UsersDataSource
dataprovider.users.strategy = stop_on_end
dataprovider.users.usage = copied
```

## Multiple data sources

It's possible to use multiple data sources in one test.

### Strategies

In case of multiple data sources the test is executed the number of times that corresponds to the length of the shortest data source.ie. It will be
executed for <span style="color:#ba3925;">min(dataSource1.size, dataSource2.size, ...)</span> times.

If any of the data sources has strategy <span style="color:#ba3925;">STOP_ON_END</span>, test execution count will be equal to the amount of the rows in the smallest data source.

If you use multiple data sources, and want your test to be executed until stopped, all of the used data sources have to have the <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> strategy.

**<span style="color:#ba3925;">Example 1: Two data sources with the default (STOP_ON_END) strategy.</span>**

**Data source "Users"**

username   | password
:--------- | :---------
user1      | psw1
user2      | psw2

**Data source "MailContent"**

recipient  | subject
:--------  | :--------
userA      | mail1
userB      | mail2
userC      | mail3

**Invocations of test**

username  | password | recipient | subject
:-------  | :------- | :-------  | :------
user1     | psw1	 | userA     |  mail1
user2     | psw2     | userB     |  mail2

**Note:** There will be two executions because there are only two rows of data in the 'Users' data source.

**<span style="color:#ba3925;">Example 2: Two data sources with the different data provision strategies.</span>**

**Data source "Users"** (<span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> (cycles))

username  | password
:-------  | :--------
user1     | psw1
user2     | psw2

**Data source "MailContent"** (<span style="color:#ba3925;">STOP_ON_END</span> (empty after the last row of data is read))

recipient | subject
:-------- | :--------
userA     | mail1
userB     | mail2
userC     | mail3

**Invocations of test**

username | password | recipient | subject
:------- | :------  | :-------- | :-------
user1    | psw1	    | userA     |  mail1
user2    | psw2     | userB     |  mail2
user1    | psw1     | userC     | mail3


**Note:** The last invocation used the first row from data source A - because its data was fully read on iteration 2 (it has only 2 rows), but due
to <span style="color:#ba3925;">REPEAT_UNTIL_STOPPED</span> strategy the reader pointer was reset and on iteration 3 it points to first row again. However, the overall
invocation amount was 3, because there are just 3 rows in data source B, and data source B is not a cycling data source.


## Empty data sources

If a flow has a Data Source which contains no records, it will not execute at all. This can be confusing for two reasons:

1.  As nothing will be executed, and no assertions tested, test will be considered passed in reports.
2.  Flows that depend on the result of not executed flow will fail, and it will be unclear that the root problem resides in
the flow with empty Data Source.


**To address this issue as of TAF version 2.31.1 the default behavior for scenarios running with empty datasources will be to fail the test.**



**Note:** It is not recommended to use scenarios with empty datasources

If it is necessary for a scenario to be able to run with an empty datasouce there are two options.

### 1. Set Allow empty on a single datasource  **(Not recommended)**

```java                               ),
    flow(sendEmail)
        .addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
        .withDataSources(dataSource('users').allowEmpty()),
```java

By using the allowEmpty() datasource method the above flow will not fail if no users are registered in the "users" datasource.

### 2. Set Allow empty globally on all datasources **Note:** (Not recommended)

If you need to allow a scenario to run with empty datasource without failing for this reason, you can set the global property "allowEmptyDataSources"
as below

```java
    System.setProperty("allowEmptyDataSources", "false");
```java

**Note:** This needs to be set prior to running the scenario, i.e. not in a setup flow as it will not be picked up afterr the scenario has begun

```java

System.setProperty("allowEmptyDataSources", "false");

TestScenario sendEmailScenario = scenario("Send email")
        .addFlow(sendEmail)
        .addFlow(sendReplyEmail)
        .addFlow(receiveEmail)
        .withScenarioDataSources(dataSource("users"))
        .build();
```java
