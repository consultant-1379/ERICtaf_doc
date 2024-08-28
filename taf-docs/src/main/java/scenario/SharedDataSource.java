package scenario;


import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.datasource.*;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import javax.inject.Inject;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.taf.datasource.TafDataSources.fromCsv;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class SharedDataSource extends TorTestCaseHelper {

    @Inject
    TestSteps steps;

    @Test
    @TestId(id = "CIP-xxx_Perf1", title = "Shared Data Source")
    public void test() {
        final String SHARED_DS = "sharedDs";

        TestDataSource<DataRecord> csvDataSource = fromCsv("data/node.csv");

        TestDataSource shared = TafDataSources.shared(csvDataSource);

        TafTestContext.getContext().addDataSource(SHARED_DS, shared);

        TestStepFlow fmFlow = flow("Test")
                .addTestStep(annotatedMethod(steps, "Login"))
                .withDataSources(
                        dataSource(SHARED_DS)
                )
                .build();

        TestScenario scenario = scenario()
                .addFlow(fmFlow)
                .withVusers(3)
                .build();

        runner().build().start(scenario);
    }
}
