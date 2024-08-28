package datadriven;

import com.ericsson.cifwk.taf.annotations.DataSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomDataSource {

    // Result should implement java.lang.Iterable<Map<String,Object>>
    @DataSource
    public List<Map<String,Object>> dataSource() {
        Map<String, Object> data = Collections.<String, Object>singletonMap("Pi", "3.14");
        return Collections.singletonList(data);
    }

}
