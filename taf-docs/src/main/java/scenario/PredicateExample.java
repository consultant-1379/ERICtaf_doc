package scenario;


import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.datasource.DataRecord;
import com.google.common.base.Predicate;
import javax.inject.Inject;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;


public class PredicateExample extends TorTestCaseHelper {

    @Inject
    TestSteps steps;

    @Test
    @TestId(id = "CIP-xxx_Perf1", title = "Predicate example")
    public void test() {
        //...
        scenario().addFlow(
                flow("flow").addTestStep(annotatedMethod(steps, "Login"))
                        .withDataSources(
                                dataSource("dataSource").withFilter(new Predicate<DataRecord>() {
                                    @Override
                                    public boolean apply(DataRecord dataRecord) {
                                        return "1".equals(dataRecord.getFieldValue("nodeId"));
                                    }
                                })
                        ));
        //...
    }
}
