<head>
   <title>JBOSS Handler</title>
</head>

# JBOSS Handler

## What is JBOSS?

JBOSS stands for JavaBeans Open Source Software and is a free software/open-source Java EE-based application server.
More information can be found at http://www.jboss.org

## What is the TAF JBOSS Handler?

The TAF JBOSS handler is an API which simplifies the interactions with JBOSS.

## How to get started

Firstly you need to create an instance of the JBOSSHandler

```java
    JbossHandler jboss = new JbossHandler(jbossNode, serverThatHostsJbossInstance)
```java

The first argument is a [Host object](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/taf/data-handler/apidocs/com/ericsson/cifwk/taf/data/Host.html) which is the jboss node you want to interact with. The second optional argument is another host
object which is the server the node is located on if you are connecting to it remotely. If this argument is not specified, remote access
to Jboss is used only.

:------- | :--------
**NOTE** | Please note to use deployment service, management port will need to be specified in Host object as well as Management Real user.

Please note to use JMX service of JBOSS handler, JMX port needs to be specified in the host object as well as Management Realm user.

Once you have instantiated the jboss handler you can deploy, activate or undeploy a file.

```java
    boolean deployed = jboss.deployFile(file)
```java

The deployFile method’s first argument is the file to deploy, in the form of a file object. It also has 2 optional arguments. First is a
boolean activate, this specifies whether the file should be executed once it is deployed. The second optional argument is another boolean
which decides whether to force deploy the file or not, if this is set to true the file will be deployed regardless of the current state.

```java
    boolean activated  = jboss.activateDeployedFile(filename)
```java

The activate deployed file has just one argument, the string filename of the file to activate.

```java
    boolean undeployed = jboss.undeployFile(filename)
```java

The undeploy file method’s first argument is the string filename to undeploy. It also has an optional second argument, this is a boolean,
which if set to true removes the file from the jboss node filesystem.

:------- | :--------
**NOTE** | When an instance of the JbossHandler is created 2 more objects are created in the constructor, an instance of JBOSS Command Executor and JMX Handler. How these can be used to work with your jboss node are detailed in pages under this page. JMX Handler instance should be closed in your test cases as follows. See JMX Javadoc for more information.

```java
    void close();
```java

## Current Functionality

Deploy, activate and undeploy a file