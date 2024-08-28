<head>
   <title>Jboss handler code snippet</title>
</head>

# Jboss handler code snippet

```java
package com.ericsson.cifwk.taf.example;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.JbossCommandExecutor;
import com.ericsson.cifwk.taf.handlers.JbossHandler;
import com.ericsson.cifwk.taf.handlers.JmxHandler;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.cifwk.taf.utils.FileUtils;

public class JbossHandlerExample {
    private static Logger logger = Logger.getLogger(JbossHandlerExample.class);
    private String jbossNodeName = "eap";
    private String fileToDeployName = "war";
    private JbossHandler handler;

    public Host getNode() {
        return DataHandler.getHostByName(jbossNodeName);
    }

    public Host getLitpServer() {
        List<Host> allHosts = DataHandler.getHosts();
        for (Host host : allHosts) {
            for (Host node : host.getNodes()) {
                if (node.getHostname().equals(jbossNodeName)) {
                    logger.debug("Got LITP server " + host);
                    return host;
                }
            }
        }
        throw new NullPointerException("No LITP host with JBOSS nodes found");
    }

    public File getFileToDeploy() {
        List<String> fileNames = FileFinder.findFile(fileToDeployName);
        if (fileNames.size() == 0)
            fileNames = FileFinder.findFile(fileToDeployName, new File(FileUtils.getCurrentDir()).
            getParentFile().getAbsolutePath());
        return new File(fileNames.get(0));
    }

    @Test
    public void exampleOfHandlerInstantation() {
        handler = new JbossHandler(getNode(), getLitpServer());
    }

    @Test(dependsOnMethods = {"exampleOfHandlerInstantation"})
    public void exampleOfDeployingFile() {
        final boolean deployAndActivate = true;
        final boolean forceDeploy = false;
        handler.deployFile(getFileToDeploy(), deployAndActivate, forceDeploy);
    }

    @Test(dependsOnMethods = {"exampleOfHandlerInstantation", "exampleOfDeployingFile"})
    public void exampleOfUndeployingFile() {
        final boolean deactivateAndRemoveFile = true;
        handler.undeployFile(getFileToDeploy().getName(), deactivateAndRemoveFile);
    }

    @Test(dependsOnMethods = {"exampleOfHandlerInstantation"})
    public void exampleOfRunningCommand() {
        final String pwd = "pwd";
        final String incorrectCommand = "incorrectcommand";
        final JbossCommandExecutor cmdService = handler.getCommandService();
        logger.debug("Execution output " + cmdService.execute(pwd));
        logger.debug("Execution response: " + cmdService.getResponse());
        try {
            cmdService.execute(incorrectCommand);
        } catch (Throwable error) {
            logger.info("Execution error: " + error);
        }
    }

    @Test(dependsOnMethods = {"exampleOfHandlerInstantation"})
    public void exampleOfMBeanGetting() {
        final String mbeanName = "java.lang:type=OperatingSystem";
        final String procInfo = "AvailableProcessors";
        final JmxHandler jmxService = handler.getJmxService();
        logger.info("Available processors: " + jmxService.getMBean(mbeanName)
        .getProperty(procInfo));
    }
}
```java
