<head>
   <title>Scenarios - Flow</title>
</head>

# Flow

## Definition

Technical implementation of a scenario. It is an immutable sequence of test steps operating on same data records. The sequence will
be repeated for whole set of data records eg each row of csv file.

## Combining Test Steps
When thinking about flows it is important to have in mind the data that will need to be passed between flows. This will impact the
test steps included in flow and the order the flows will be executed. It is also important to note that test steps are reusable and
may be used more than once within your flow.

In our example we have one scenario that sends an email. We can look at this at 3 separate subflows as data is passed from one flow
to the next. We should also note that steps are repeated within these subflows. Each subflow has a login and logout steps. These flows
will reuse the following test steps.

**<span style="color:#ba3925;">Test Steps</span>**

* login
* select mail action - compose
* set mail fields
* send email
* open mail folder - received
* open mail
* select mail action - reply
* logout

**<span style="color:#ba3925;">Flows</span>**

Out of those test steps multiple flows can be composite. In this example:

**Flow 1**
_Send Email_

* login
* select mail action - compose
* set mail fields
* send email
* logout

**Flow 2**
_Receive Email and Reply_

* login
* open mail folder - received
* open mail
* select mail action - reply
* set mail fields
* send email
* logout

**Flow 3**
_Receive Email_

* login
* open mail folder - received
* open mail
* logout


### Creating Flows

TAF provides the following convenience method for creating flows.

```java
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;

....

TestStepFlow sendEmail= flow("Login to email server and send email")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "composeEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "setMailFields"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "sendEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.withDataSources(dataSource("users"),dataSource("mailContent"))
	.build();

TestStepFlow sendReplyEmail= flow("Receive Email and Reply")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "openMailFolder"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "openMail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "reply"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "setMailFields"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "sendEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.withDataSources(dataSource("users"),dataSource("sentEmails"))
	.build();

TestStepFlow receiveEmail= flow("Receive Email")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "openMailFolder"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "openMail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.withDataSources(dataSource("users"),dataSource("replyEmails"))
	.build();
```java

### Define sequence

Add Test Steps in the required order of execution, Test Steps are executed in the order in which they are added to the flow.

```java
TestStepFlow sendEmail= flow("Login to email server and send email")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "composeEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "setMailFields"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "sendEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.build();
```java

### Define DataSource

All Data Sources required for execution of Test Step should be added using <span style="color:#ba3925;">.withDataSources()</span> method.

```java
TestStepFlow sendEmail= flow("Send email")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "composeEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "sendEmail"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.withDataSources(dataSource("users"),dataSource("mailContent"))
	.build();
```java

### Passing Data Between Test Steps and Flows

Test Step is a method that could be either void or return value. It is possible to pass that data to a subsequent test step or flow.

<span style="color:#ba3925;">Returning Data from a Test Step</span>

Simply give the test step method a return type and return a value.

```java
@TestStep(id="login")
public EnmUser login(@Input("user") EnmUser user){
    Tool tool = loginOperator.login(user);
    assertThat(tool.loggedIn()).isTrue();
    return user;
}
```java

The returned variable is stored in the context and is identified by the test step id. This variable is now available as data input to a subsequent
test step in the flow. It can also be used in a subsequent flow by collecting the variable for each iteration into a datasource. This datasource
can then be used as normal in a subsequent flow.

In the case where there's more than one value to return from the Test Step you can [create a DataRecord](data_record.html) which you can return
from the test step.

```java
@TestStep(id="login")
public DataRecord login(@Input("user") EnmUser user){
    Tool tool = loginOperator.login(user);
    assertThat(tool.loggedIn()).isTrue();
    DataRecordBuilder builder = new DataRecordBuilder();
    builder.setField("username",user.getUsername()).setField("password",user.getPassword());
    return builder.build();;
}
```java

<span style="color:#ba3925;">Using the value in a subsequent test step</span>

To use data returned from a test step in a subsequent test step in the same flow add it as parameter using .withParameter and get the value using
the static method TestScenarios.fromTestStepResult(&lt;Test Step ID&gt;)

**<span style="color:#ba3925;">LoginLogoutTestSteps.java</span>**

```java
@TestStep(id="login")
public EnmUser login(@Input("user") EnmUser user){
    Tool tool = loginOperator.login(user);
    assertThat(tool.loggedIn()).isTrue();
    return user;
}

@TestStep(id="logout")
public void login(@Input("user") EnmUser user){
    setTool();
    Tool tool = loginOperator.logout(user);
    assertThat(tool.loggedIn()).isFalse();
}
```java

**<span style="color:#ba3925;">Flows.java</span>**

```java
public Flow replyToEmail(){
    return flow("checkEmail")
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"login"))
            .addTestStep(annotatedMethod(emailTestSteps, "openEmail"))
            .addTestStep(annotatedMethod(emailTestSteps, "reply"))
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"logout")
                                       .withParameter("user",fromTestStepResult("login")))
            .build();
}
```java

<span style="color:#ba3925;">Using the values in a subsequent flow</span>

To use the data returned from a test step in a subsequent flow you need to collect the values into a datasource using the chained
method collectResultToDatasource

**<span style="color:#ba3925;">Flows.java</span>**

```java
public Flow replyToEmail(){
    return flow("checkEmail")
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"login"))
            .addTestStep(annotatedMethod(emailTestSteps, "openEmail"))
            .addTestStep(annotatedMethod(emailTestSteps, "reply")
                                    .collectResultToDatasource("emailsSent"))
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"logout")
                                    .withParameter("user",fromTestStepResult("login")))
            .build();
}

public Flow checkSentEmails(){
    return flow("checkEmail")
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"login"))
            .addTestStep(annotatedMethod(emailTestSteps, "openSentItems"))
            .addSubFlow(flow("Check Emails")
                                        .addTestStep(annotatedMethod(emailTestSteps,"verifyEmailRecipientPresent"))
                                        .withDataSource(datasource("emailsSent")))
            .addTestStep(annotatedMethod(loginLogoutTestSteps,"logout")
                                        .withParameter("user",fromTestStepResult("login")))
            .build();
}
```java

### Before Flow / After Flow

Flows support the possibility to execute code before and after running test steps. It may be useful for data source setup and transformation.

* <span style="color:#ba3925;">beforeFlow/afterFlow</span> methods accept objects that implements <span style="color:#ba3925;">java.lang.Runnable</span>
* <span style="color:#ba3925;">afterFlow</span> will be executed even in case scenario fails (one of flows throws unhandled exception)
* Before/After flow code is not dependant on vUser count and will execute only one time. If you need to execute cleanup
  for each vUser consider using **Always Run Test Step**
* It is possible to pass multiple runnables in one Before/After flow method
* To simplify data source transformations following methods in <span style="color:#ba3925;">TafDataSources</span> are available:
	* <span style="color:#ba3925;">TafDataSources.copyDataSource(String name)</span> - replace data source with given name with its **copy**.
	* <span style="color:#ba3925;">TestScenarios.shareDataSource(String name)</span> - make data source with given name **shared**.
	* <span style="color:#ba3925;">TafDataSources.makeDataSourceCyclic(String name)</span> - make data source with given name **cyclic**.

Example:

```java
flow("sendMail")
        .beforeFlow(
                copyDataSource(mailsToSend),
                shareDataSource(USERS))
        .addTestStep(annotatedMethod(this, "login"))
        .addTestStep(annotatedMethod(this, "composeEmail"))
        .addTestStep(annotatedMethod(this, "sendEmail"))
        .addTestStep(annotatedMethod(this, "logout"))
        .afterFlow(new Runnable() {
            @Override
            public void run() {
                closeMail()
                cleanDataSources();
            }
        })
    }
```java

### Wait Between Flows

It is possible to call a wait method from within a flow without having to implement a test step specifically for that reason.

:----------  | :------------
**WARNING:** | This is not advised in most situations as waiting for events is preferable but is available when strictly necessary.

```java
TestStepFlow sendEmail= flow("Scenario flow")
	.addTestStep(annotatedMethod(sendEmailTestSteps, "login"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "step1"))
	.wait(2000)
	.addTestStep(annotatedMethod(sendEmailTestSteps, "step2"))
	.addTestStep(annotatedMethod(sendEmailTestSteps, "logout"))
	.build();
```java
