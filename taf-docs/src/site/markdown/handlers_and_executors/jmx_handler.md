<head>
   <title>JMX Handler</title>
</head>

# JMX Handler

## What is JMX?

JMX stands for Java Management eXtensions which is a Java technology that supplies tools for managing and monitoring applications, system
objects, devices (e. g. printers) and service oriented networks

## What is the TAF JMX Handler?

The TAF JMX Handler is an API which allows jmx connection to a JBoss AS which will allow you to monitor and manage applications in the
jboss instance.

## How to get started

Create an instance of JMXHandler

```java
    JmxHandler jmx = new JmxHandler(host)
    /* OR */
    JmxHandler jmx = new JmxHandler(ip, username, pass, jmxPort)
```java

The JmxHandler has 2 constructors, one which takes a [Host object](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/data/Host.html) and the other which takes all the host details individually i.e. username, password, ip, etc.

:------- | :---------
**NOTE** | Host object requires admin user and jmx port to be set

With JMX handler you can get a particular MBean or a list of MBeans from the JBoss instance

```java
    GroovyMBean mBean = jmx.getMBean(mBeanName)
    List<String> beanList = jmx.getMBeansList(string query) // Query defaults to all
```java

mBean has all the operations and attributes available via injection or via setProperty/getProperty and invokeMethod helpers.

:------- | :---------
**NOTE** | JMX connection to JBoss server is re-used and monitored for idle time (set by default to 10 seconds). This value can be changed by setting connectionTimeout static property.

## Current Functionality

Create a connection to the JMX port on a Jboss instance and get a particular mbean or a list of them.

##  Planned Improvements

None at this time

## Known issues and workarounds

None at this time


