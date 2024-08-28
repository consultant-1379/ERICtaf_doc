Title: Did you know you can now reference properties in data?
Slug: embedded-properties-in-data
Date: 2016-11-30 15:24
Category: TAF
Tags: new feature
Authors: Thomas Melville
Status: published
Summary: Don't you just hate it when you have to duplicate information in your data? Now you can specify property keys in your data and they will be replaced at runtime.

Following on from the blog post on [embedding properties within properties](embedded-properties.html) it is now possible to embed properties in data and they will be replaced at runtime.

Let's say you need to add nodes to the SUT and then copy these nodes to a non-live configuration.
 
This is how your data would look:

nodesToAdd.csv
```text
networkElementId,nodeType,port,transportType,secureUserName,secureUserPassword,ossPrefix,platformType
SGSN-16A-CP01-V101,SGSN-MME,22,SSH,netsim,netsim,,
LTE06dg2ERBS00001,RadioNode,6513,TLS,netsim,netsim,,
LTE02ERBS00002,ERBS,,,netsim,netsim,,CPP
K3C119301,MGW,,,netsim,netsim,,CPP
```

configCreate.csv
```text
createConfig,expectedInCreateResponse
cmFeatureConfig1,Config successfully created
cmFeatureConfig2,Config successfully created
cmFeatureConfig3,Config successfully created
cmFeatureConfig4,Config successfully created
```

configCopy.csv
```text
copySourceConfig,copyTargetConfig,copyNodeFilter
Live,cmFeatureConfig1,SGSN-16A-CP01-V101
Live,cmFeatureConfig2,LTE06dg2ERBS00001
Live,cmFeatureConfig3,LTE02ERBS00002
Live,cmFeatureConfig4,K3C119301
```

As you can see the networkElementIds (networkElementId, copyNodeFilter) and configuration names (createConfig, copyTargetConfig) are repeated across the csv files.

Wouldn't it be great to specify them in one place and reuse them from there!

properties file
```properties
nodes.myNode1=SGSN-16A-CP01-V101
nodes.myNode2=LTE06dg2ERBS00001
nodes.myNode3=LTE02ERBS00002
nodes.myNode4=K3C119301

configs.myConfig1=myConfig1
configs.myConfig2=myConfig2
configs.myConfig3=myConfig3
configs.myConfig4=myConfig4
```

and now here's the updated data files with no repetition

nodesToAdd.csv
```text
networkElementId,nodeType,port,transportType,secureUserName,secureUserPassword,ossPrefix,platformType
${nodes.myNode1},SGSN-MME,22,SSH,netsim,netsim,,
${nodes.myNode2},RadioNode,6513,TLS,netsim,netsim,,
${nodes.myNode3},ERBS,,,netsim,netsim,,CPP
${nodes.myNode4},MGW,,,netsim,netsim,,CPP
```

configCreate.csv
```text
createConfig,expectedInCreateResponse
${configs.myConfig1},Config successfully created
${configs.myConfig2},Config successfully created
${configs.myConfig3},Config successfully created
${configs.myConfig4},Config successfully created
```

configCopy.csv
```text
fTestCaseId,copySourceConfig,copyTargetConfig,copyNodeFilter
TC_0001,Live,${configs.myConfig1},${nodes.myNode1}
TC_0002,Live,${configs.myConfig2},${nodes.myNode2}
TC_0003,Live,${configs.myConfig3},${nodes.myNode3}
TC_0004,Live,${configs.myConfig4},${nodes.myNode4}
```

**Please note the following**

When using a DataRecord extension, the substitution is done when the get method is called, so the Scenario Logging 
will show the key and not the value.

Example:

```text
2017-10-27 10:20:22,703 [Set PIB Parameters in VNF LAF.Set PIB Parameters flow.vUser-1] [INFO] [com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener] VUser: 1 Starting test step : VnfLafTestSteps.setPibParameter(Data value: {paramName=ossType, paramValue=${scripting.osstype}})
```

I hope this blog post has been helpful to you, and the next time you see repetition in data you'll think about creating properties.
