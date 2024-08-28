package scenario;

import com.ericsson.cifwk.taf.annotations.*;
import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

// Create a TestSteps class.
public class TestSteps {

    // Inject an instance of any operators you wish to use.
    @Inject
    LoginOperator loginOper;

    @Inject
    RestOperator restOper;

    // Create methods annotated with TestStep and give them unique ids.
    @TestStep(id = "Login")
    public void login(@Input("user") String user, // Annotate the variables to the methods with
                      @Input("pass") String pass, // Input/Output
                      @Input("host") String host, // The same as you would when writing data
                      // driven functional tests.
                      @Output("Result") String result) {

        // In each method interact with the SUT through the operator and verify the interaction
        assertThat(loginOper.login(host, user, pass), equalToIgnoringCase(result));
    }

    @TestStep(id = "calls")
    public void restCalls(@Input("url") String url,
                          @Input("params") String[] params,
                          @Input("host") String host,
                          @Output("response") String response) {

        assertThat(restOper.call(host, url, params), equalToIgnoringCase(response));
    }
}
