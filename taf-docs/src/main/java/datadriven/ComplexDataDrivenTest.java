package datadriven;

import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import org.testng.annotations.Test;

public class ComplexDataDrivenTest {

    @Test
    @DataDriven(name = "calculator")
    public void dataDrivenTest(
		@Input("list") String[] list,
    		@Input("option") int option,
    		@Input("text") String text) {
        // test code
    }
}
