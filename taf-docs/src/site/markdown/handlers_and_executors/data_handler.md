# Data Handler

<div class="note"></div>
This is deprecated documentation. For up-to-date version please refer to 
[TAF Data Handler](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/snapshot/handlers_and_executors/taf_data_handler.html).

## What is Data?

Data is what drives testing in TAF. This can be data about the system, data used to test the system, etc.

## What is the TAF Data Handler?

The TAF Data Handler is a central point of data management.

It retrieves all the data from various locations and makes it available for test code.

This way, data specified in properties files or OS environment variables are available using one method call.

Data Handler also allows users to store values and fetch them e.g. in different thread.

:------- | :---------
**NOTE** | Data set at run time using the Data Handler takes precedence over data set via environment variables, which themselves take precedence over data set from from property files.

Some attribute *attributeA = propertyFileValue*

The same attribute could be set via environment variable *java -DattributeA = environmentVariableValue*

Same attribute could be set in code at run time *DataHandler.setAttribute=(“attributeA”,“runTimeValue”);*

If this was the case the runtime value would be used. More detail below.

## How to get started

The Data Handler methods can be accessed statically so there is no need to instantiate it.

```java
    /* Get a system property */
    String classpath = DataHandler.getAttribute("java.class.path")

    /* Get a property defined by you */
    String variableSpecificToYourTest = DataHandler.getAttribute("path.to.file.specified.by.you")

    /* Get a map of all the properties, user defined, system and runtime. */
    Map allAttributes = DataHandler.getAttributes()

    /* Get the list of Hosts defined by you */
    List<Host> hosts = DataHandler.getHosts()

    /* Get all the Hosts by type (for Netsim for example) */
    List<Host> hosts = DataHandler.getAllHostsByType(HostType.NETSIM)
```java

Adding arguments from property files or OS variables:

```java
    testware.properties:
    firstProperty = first
    secondProperty = 2

    OS environment:
    java -DthirdProperty=3 -DsecondProperty=two ...
```

Adding arguments from property files or OS variables in your testware:

**Note:** runtime properties have higher priority than OS environment variables

```java
    DataHandler.getAttribute("firstProperty") //(returning Object-String "first")
    DataHandler.getAttribute("thirdProperty") //returning Object-String "3")
    DataHandler.getAttribute("secondProperty") // returning Object-String "two" as OS environment
    variable has higher priority than property in file
    DataHandler.setAttribute("secondProperty",2)
    DataHandler.getAttribute("secondProperty") //returning Object - integer 2 as properties set at
```java

It is also possible to set an attribute at runtime

```java
    DataHandler.setAttribute("path.to.file.specified.by.you", "/home/myfile.txt")
```java

## Loading configuration from HTTP servlet

In TAF version 2.0.12 a new reconciler HttpConfigDataReconciler was added - it loads data (hosts and properties) from the URL defined in runtime system property *<span style="color:#ba3925;">taf.http_config.url.</span>*

HttpConfigDataReconciler will load the host JSON data from <span style="color:#ba3925;">&lt;URL&gt;?type=hosts</span>, and properties from <span style="color:#ba3925;">&lt;URL&gt;?type=properties</span>

Example:

```
    mvn test -Dtaf.http_config.url=http://ess_web/servlets/taf_config
```

In this case http://ess_web/servlets/taf_config?type=hosts and http://ess_web/servlets/taf_config?type=properties will be requested, and response will be parsed.
Expected content type of hosts data is <span style="color:#ba3925;">application/json</span>, and properties data content type - <span style="color:#ba3925;">text/plain</span>.

HTTP servlet configuration data has a high priority, so it will override any settings loaded from system properties, files, etc. if they have the same name.

## API Documentation Link

[DataHandler API Documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/data/DataHandler.html)

## Current Functionality

Combine data from property files, HTTP servlet, OS environment settings and runtime data to be available via method call.

Get and set attributes/properties in runtime.
