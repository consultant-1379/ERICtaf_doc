<head>
   <title>Scenarios - Runner</title>
</head>

# Runner

## Definition

The Runner is the engine to execute the scenario in a controlled way. It takes care of threading mechanism in JVM. It also allows to
specify logging and exception handling mechanism.

## Executing Scenarios
Scenario is run using the <span style="color:#ba3925;">start()</span> method.

```java
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
.....

TestScenarioRunner runner;

@Test
public void sendEmailTest(){
....
runner = runner().build();
runner.start(sendEmailScenario);
}
```

By default runner will log no scenario execution information and it will stop execution on first exception.

## Logging execution progress

TAF provides simple logging using scenarioListener

```java
runner().withListener(new LoggingScenarioListener())
```

Create Listener with given priority. Listeners with highest priority are called first. Default priority is 0.

```java
runner().withListener(new LoggingScenarioListener(), int priority)
```
