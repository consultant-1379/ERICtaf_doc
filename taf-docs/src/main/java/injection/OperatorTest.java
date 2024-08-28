package injection;

import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Provider;

public class OperatorTest {

    @Inject
    Provider<Operator> provider;

    @Test
    public void shouldProvideOperatorsBasedOnContext() {
        Operator operator = provider.get();
    }

}