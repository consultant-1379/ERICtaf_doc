package datadriven;

import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import org.testng.annotations.Test;

public class DataDrivenTest {

    @Test
    @TestId(id = "TAF_Scenarios_0092")
    @DataDriven(name = "calculator")
    public void dataDriven(@Input("x") int x, @Input("y") int y, @Output("z") int z) {
        // test code
    }

    @Test
    @DataDriven(name = "calculator")
    public void dataDrivenWithTestId(@Input("x") int x, @TestId @Input("y") String testId, @Output("z") int z) {
        // test code
    }

}