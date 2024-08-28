package scenario;


import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import javax.inject.Inject;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class SplitMerge extends TorTestCaseHelper {

    @Inject
    TestSteps steps;

    @Test
    @TestId(id = "CIP-xxx_Perf1", title = "Execute flows in parallel")
    public void test() {
        TestScenario scenario = scenario("split-merge")
                .addFlow(
                        flow("sequential1").addTestStep(annotatedMethod(steps, "Login"))
                ).split(
                        flow("parallel1").addTestStep(annotatedMethod(steps, "calls")),
                        flow("parallel2").addTestStep(annotatedMethod(steps, "calls")),
                        flow("parallel3").addTestStep(annotatedMethod(steps, "calls"))
                )
                .addFlow(
                        flow("sequential2").addTestStep(annotatedMethod(steps, "calls"))
                )
                .withVusers(2)
                .build();

        runner().build().start(scenario);
    }
}
