<head>
   <title>Scenarios - Vusers</title>
</head>

# Vusers

## Definition

A virtual user is a model mimicking the full flow of actions performed by one customer. To make the model similar to real life situations, different customer
sensitive data eg customer username, password should only be used by one Vuser at a time. This way allows you to simulate the load of a number of customers
working on the system at the same time. By increasing/decreasing number of Vusers we can control the load of the system as desired.

## Vuser Boundary

Isolation of Vuser is essential from perspective of providing real life simulation of system load. For example using the same user credentials for every
Vuser would make this data cached rather than fetched from data base every time. This could potentially hide user database performance issues.

**<span style="color:#ba3925;">Test Context</span>**

For the convenience of keeping the Vuser related data isolated, the mechanism of injectable TestContext is available in TAF. Every Vuser has a separate
TestContext that can be populated only inside Vuser flow and this information is not shared with any other Vuser.

```java
@Inject
TestContext context;

context.setAttribute("username","ejohsmi");

context.getAttribute("username");
```java

From the code above you can see that any type of user related attributes can be stored in local context eg user login. Those attributes are passed between
test steps and flows for a particular Vuser.

**<span style="color:#ba3925;">User Session</span>**

One of the good practices is to keep status of interfaces with SUT in a specialised bean. For example keeping an instance of transactional tool (browser)
in User Session is strongly recommended. As user interacts with SUT some of the information delivered from SUT is changing the tool status, most common
examples would be when user logs in or logs out. Convenient implementation of user session could look like this.

```java
public class UserSession {
	public static final String SESSION = "session";

	private Tool tool;

	public void setTool(final Tool tool) {
		this.tool = tool;
	}

	public Tool getTool() {
		return tool;
	}
}
```java

## Data Source Usage
Depending on the use case some of the data needs to be used only once during scenario and some can be used multiple times.
To support both cases TAF provides the following usage possibilities.

**<span style="color:#ba3925;">Shared</span>**

Records in this data source are available for all Vusers from the same pool. It means that if first Vuser uses the first record its not available to
any other Vuser.

:-------  | :--------
**NOTE:** | When number of Vusers outnumbers size of data source REPEAT_UNTIL_STOPPED can be used to ensure all Vusers have possibility to access data.

:-------  | :--------
**NOTE:** | When size of data source is greater than the number of Vusers, a Vuser may run multiple times until the data source is exhausted.

**<span style="color:#ba3925;">Copied</span>**

A new copy of full set of data records is provided for each Vuser. This means that the first record will be available for each Vuser.

For more details about data sources, see **Basic Data Concepts** section.

## Vuser on flows Vs Vuser on scenarios

Vuser can be specified on both flow and scenario. Flow level Vusers take precedence over those specified on scenario. If no Vuser is specified on
either flow or scenario Vuser defaults to 1.

## VUserScoped

In many cases keeping same instance of operator for single Vuser is necessary. It may happen that operator is stateful and cares about previous
methods invoked. TAF provides simple mechanism to achieve it if injection is used. Operator class can be annotated with <span style="color:#ba3925;">@VUserScoped</span> marker. It means
that there will be one instance of operator created for Vuser and all future references to it, will re-use same instance. It is possible to inject <span style="color:#ba3925;">@VUserScoped</span>
operators inside each other resulting the same scope features.

```java
    @Inject
    Provider<StatefulOperator> provider;

    @Test
    public void vUserScope() {
        TestScenarioRunner runner = runner().build();
        TestScenario build = scenario()
                .withVusers(30)
                .addFlow(flow("sendEmail").addTestStep(annotatedMethod(provider, login)))
                .addFlow(flow("sendReplyEmail").addTestStep(annotatedMethod(provider, composeEmail)))
                .build();
        runner.start(build);
    }

    @Operator
    @VUserScoped
    public static class StatefulOperator {

        private String state;

        @TestStep(id = login)
        public void login() {
            openLoginPage();
            enterLoginDetails();
        }

        @TestStep(id = composeEmail)
        public void composeMail() {
            openEmail()
            populateFields();
        }

    }
```java

**Please note that operator instance is never received in test code.** Only operator provider is passed into test step factory.
