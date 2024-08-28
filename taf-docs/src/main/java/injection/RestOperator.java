package injection;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;

import javax.inject.Singleton;

@Operator(context = Context.REST)
@Singleton
public class RestOperator {
    // ...
}