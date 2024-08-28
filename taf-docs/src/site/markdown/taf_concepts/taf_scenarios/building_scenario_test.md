<head>
   <title>Scenarios - Building a Scenario Test</title>
</head>

# Building a Scenario Test

Scenario tests are built by combining flows.

## Combining Flows

In our example of sending an email, there are 3 flows which need to be executed sequentially.

```java
import com.ericsson.cifwk.taf.scenario.TestScenario;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

.....

TestScenario sendEmailScenario = scenario("Send email")
        .addFlow(sendEmail)
        .addFlow(sendReplyEmail)
        .addFlow(receiveEmail)
        .build();
```java

:-------  | :--------
**NOTE:** | The execution will happen in the following way:

* Send email flow will be executed for all data records available in data source

* Then send reply email will execute for all data records in sentEmails data source.

* Eventually receive email flow will execute for all data records created in send reply email flow.
