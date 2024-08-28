<head>
   <title>Code snippet for using properties</title>
</head>

# Code snippet for using properties

```java
package com.ericsson.cifwk.taf.example;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;

public class HostPropertiesExample {

    private static Logger logger = Logger.getLogger(HostPropertiesExample.class);

    @Test
    public void getEnvironmentVariable(){
        String variable = "strangeEnv";
        logger.info("Value of " + variable + " is: " + DataHandler.getAttribute(variable));
        variable = "setVariable";
        logger.info("Value of " + variable + " is: " + DataHandler.getAttribute(variable));
    }

    @Test
    public void getVariableFromPropertiesFile(){
        final String variable = "monitor.mem.collectionTime";
        logger.info("Value of " + variable + " is: " + DataHandler.getAttribute(variable));
    }

    @Test(dependsOnMethods={"getVariableFromPropertiesFile"})
    public void exampleOfOverridingEnvironmentVariable(){
        final String variable = "monitor.mem.collectionTime";
        System.setProperty(variable,"10");
        logger.info("Value of " + variable + " is: " + DataHandler.getAttribute(variable));
        DataHandler.setAttribute(variable, 20);
        logger.info("Value of " + variable + " is: " + DataHandler.getAttribute(variable));
    }

    @Test
    public void listSomeInformationAboutHost(){
        final Host sc1 = DataHandler.getHostByName("sc1");
        logger.info("IP address of SC1: " + sc1.getIp());
    }
}
```java
