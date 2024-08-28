<head>
   <title>Scenarios - Test Steps</title>
</head>

# Test Steps

## Definition
Test Steps are the basic building blocks of a flow. A test step is a reusable method that contains implementation of a test action and can contain
a verification of output of this action.

## Determining Test Steps
Goal of determining test steps is to break scenario into small enough elements to enable reusability and comprehensive level of reporting.

:-------  | :--------
**NOTE:** | It's important to keep test steps self contained without direct dependency on each other. This way multiple flows can be built from the same test steps. If same data is used between different steps it's perfectly fine to reuse the same data record in all necessary steps of the flow.

### Strategies and Approaches

From our previous example lets just look at the steps involved in sending an email. Using each of the strategies mentioned, we will look at how to
break down the flow into reusable test steps.

**<span style="color:#ba3925;">Step by Step</span>**

Breakdown scenario by detailing each action that is required in the order they are carried out.

To send an email one must first login to email client, select compose mail, input required data - email address, subject, message body. They then select
send email and logout.

So the test steps might be:

* login
* select mail action - compose
* set mail fields
* send email
* logout

**<span style="color:#ba3925;">Back to Front</span>**

Breakdown scenario starting with the desired outcome and working backwards detailing the actions required to get to this point.

A user receives an email. For this user to receive an email another user must have sent an email. To send an email mail needs to be composed. To be able
to compose mail user needs to be logged in.

Again the steps might be:

* login
* select mail action - compose
* set mail fields
* send email
* logout

**<span style="color:#ba3925;">Divide and Conquer</span>**

This strategy involves recursively breaking down scenario into two or more subflows until flows become single individual actions.

Using our example we wish to send an email. And sending an email involves composing an email and then hitting send. To compose an email we
must select compose mail and input the required fields. To compose a mail one must be logged into the mail client.

* login
* select mail action - compose
* set mail fields
* send email
* logout

As you can see regardless of the approach the resulting Test Steps are the same.

## Data

There are two ways to specify data input for testStep:

1. Input/Output annotations
2. DataRecord

**<span style="color:#ba3925;">Input/Output annotations</span>**

In the case where java primitives are passed to method, Input/Output annotation specifies field name inside data source (e.g. column name in csv file).

```java
@TestStep(id = "Login")
public void login(@Input("username") String user,
                  @Input("password") String pass) {
```java

**<span style="color:#ba3925;">Data Record</span>**

In the case where DataRecord or bean is passed to method, Input/Output annotation specifies data source name to be used to construct the object.

```java
@TestStep(id = "Login")
public void login(@Input("users") User user) {
```java

DataSources can be passed between flows. For example a data source created by one flow can be used by another. This data source is created in testStep.

```java
context.dataSource("users").addRecord().setFields(user);
```java

Use <span style="color:#ba3925;">setFields(DataRecord record)</span> when adding object to dataSource. +
Use <span style="color:#ba3925;">setField(String name, Object value)</span> when adding individual values to dataSource or to update fields of record set using setFields.

```java
context.dataSource("users").addRecord()
                        .setFields(user)
                        .setField("username","admin");
```java

:-------  | :-------
**NOTE:** | It is possible but discouraged to specify name of data source on test step level. In this case a particular datasource name is always required on a flow.

### Optional Values

There may be the case that data is not mandatory to pass to the test step. In this case the annotation @OptionalValue can be used

```java

import com.ericsson.cifwk.taf.annotations.OptionalValue
...

  @TestStep(id="wait")
  public void wait(@Input("timeout") @OptionalValue int timeout){
    ...
  }

```java

In this case if timeout isn't specified in the data the Java default value for the type will be used.

If there is a default value for the parameter which is different to the java default for the type you can specify it as a String in the annotation.

```java

import com.ericsson.cifwk.taf.annotations.OptionalValue
...

  @TestStep(id="wait")
  public void wait(@Input("timeout") @OptionalValue("30") int timeout){
    ...
  }

```java

In this case if timeout isn't specified in the data it will be given the default value of 30.

#### Strict Mode

Currently strict mode for optional values is turned off. It will be turned on in the future and may break test steps if they are not updated.

With strict mode disabled if a parameter is not in the csv and not annotated with OptionalValue then a warning is printed in the logs.
When strict mode is enabled a validation exception will be thrown.


## Test Context and User Session
A very convenient mechanism for passing information between test steps is Test Context. An example sequence of test steps would be to login, send emails
and log out. As user would use one browser to perform all these steps, the information about browser status should be passed between them in the flow.
To ensure browser information is carried through flow it is recommended the browser is stored in User Session. Its changes are carried on inside User Session
and modification done in each test step are reflected. In particular each test step should set the tool that is used by operator to perform action on
SUT (Browser for UI tests, HttpTool for REST etc.)

```java
//Test Step Code
UserSession userSession = context.getAttribute(UserSession.SESSION);
operator.setTool(userSession.getTool());
```java

Logic for setting tool is provided in the operator.

```java
//Operator Code
public void setTool(Tool tool) {
    this.browser = tool.getAs(Browser.class);
}
```java

In our example the login TestStep creates the tool on login and it is this tool that is passed to each testStep.

```java
@TestStep(id = "Login")
public void login(@Input("users") User user) {
    Tool tool = operator.login(user);
    UserSession userSession = context.getAttribute(UserSession.SESSION);
    userSession.setTool(tool);
    context.setAttribute(UserSession.SESSION, userSession);
}
```java

The logout test step retrieves the tool from user session.

```java
@TestStep(id = "Logout")
public void logout() {
    UserSession userSession = context.getAttribute(UserSession.SESSION);
    Tool tool = userSession.getTool();
    .....
}
```java


### Operators and Asserts

Goal of a test step is to execute single test action and verify its output. The logic of execution lies in operator, so its test step responsibility
to inject the operator instance and pass data. Output of operator action can be passed to verification action. It is good practice to verify if business
action executed by operator returns expected results. Yet sometimes, comprehensive verification can be time consuming and can include several comparisons.
In such a case, a basic verification can be executed in test step invoking operator's methods, while additional verification steps may re-use output
of data source populated by initial step.

Here is an example of how it might look:

```java
@TestStep(id="sendEmail")
public void sendEmail(@Input("mailContent") Mail mail){
    mailOperator.sendEmail(mail);
    assertTrue(mailOperator.verifyMailIsSent(mail));
}

@TestStep(id="sentMailVerification")
public void verifySentMail(@Input("mailContent") Mail mail, @Output("email") String email, @Output("subject") String subject){
    assertEquals(mailOperator.getMailRecipient(mail),email);
    assertEquals(mailOperator.getMailSubject(mail),subject);
}
```java
