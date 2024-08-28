package scenario;


import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import javax.inject.Inject;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

// statically import the taf TestScenarios class.

// Create a test class.
public class ScenarioTest extends TorTestCaseHelper {

    // Inject the TestSteps class into the test class.
    @Inject
    TestSteps steps;

    // Create a test method
    @Test
    @TestId(id = "CIP-xxx_Perf1", title = "Execute parallel calls against the system")
    public void test() {
        // Create a TestStepFlow object.
        // Call the statically imported flow(flowName) method.
        TestStepFlow login = flow("Sample flow")
                // add 1..n teststeps to the flow.
                .addTestStep(annotatedMethod(steps, "Login"))
                .addTestStep(annotatedMethod(steps, "calls"))
                // add 0..1 datasources to the flow.
                .withDataSources(dataSource("users"))
                // specify the number of vUsers
                // this overrides the number of vUsers specified on the scenario.
                .withVusers(2)
                // build the flow.
                .build();

        TestStepFlow addNode = flow("add node")
                // ... flow definition here ...
                .build();
        TestStepFlow logout = flow("logout")
                // ... flow definition here ...
                .build();

        // Create a scenario
        // Call the statically imported scenario() method.
        TestScenario scenario = scenario()
                 // Add the flows to it
                .addFlow(login)    //this is the TestStepFlow from above
                .addFlow(addNode)
                .addFlow(logout)
                        // specify the number of vUsers
                        // This only applies to flows that don't have vusers specified.
                .withVusers(4)
                        // build it
                .build();

        // Create scenario runner
        TestScenarioRunner runner = runner().build();
        // Run the scenario
        runner.start(scenario);
    }
}
