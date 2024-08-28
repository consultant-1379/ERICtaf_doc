<head>
   <title>JBoss Command Executor</title>
</head>

# JBoss Command Executor

## What is Jboss?

Please see JBOSS Handler

## What is the TAF JBoss Command Executor?

The TAF Jboss Command Executor is an API which allows you to execute a Jboss command on a local or remote Jboss instance.

An instance of JbossCommandExecutor is created within an instance of JBossHandler for you to use to execute commands on the
Jboss instance which that JBossHandler points to. It is recommended to access JBoss command executor this way.

## How to get started

Fetch an instance of Jboss Command Executor from JBOSS Handler

```java
    JbossCommandExecutor commandExecutor = jbossHandlerInstance.getCommandService()
```java

:------- | :-----------
**NOTE** | If Jboss Handler was instantiated with 2 hosts in constructor, jboss-cli.sh will be used to execute commands. Otherwise jboss client will be use for this purpose.

It is also possible to use constructor of JbossCommandExecutor itself for getting command executor:

```java
    JbossCommandExecutor commanExecutor = new JbossCommandExecutor(Host jbossNode,Host
    serverHostingJbossInstance) //second argument is default null
    JbossCommandExecutor commanExecutor = new JbossCommandExecutor(String hostName,
    String jbossUser,String jbossPass,String jbossManagementPort,int jbossOffset,Host parentHost)
    //last 2 arguments are default to 0 and null
```java

There are 2 methods to execute a command, both are overloaded in the same way.

```java
    /*Pass the command as a single string, including the arguments in this single string */
    commandExecutor.simplExec("Command + Arguments")

    /*Pass the command and the arguments seperately. The command as a string and the arguments
    as a list of Strings */
    commandExecutor.simplExec("Command", "Argument1", "Argument2", "etc")

    /* Pass the command as a single string, including the arguments in this single string */
    commandExecutor.execute("Command")

    /*Pass the command and the arguments seperately. The command as a string and the arguments
    as a list of Strings */
    commandExecutor.execute("Command", "Argument1", "Argument2", "etc")
```java

The difference between these 2 methods is what they return.

simplExec returns the response, whereas execute returns a boolean whether the command succeeded or not. The standard and error outputs of
command execution are available via following methods:

```java
    assertTrue(jbossCommandExecutorInstance.execute("command"))
    String commandResponse = jbossCommandExecutorInstance.getResponse()
```java

:------- | :-----------
**NOTE** | All exceptions and errors sent by jboss command service are thrown as Throwable from the execution and logged as errors.

## Current Functionality

Connect to a jboss instance locally or remotely and execute commands.
