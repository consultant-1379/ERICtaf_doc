<head>
   <title>Local Command Executor</title>
</head>

# Local Command Executor

**Note:** It is highly recommended to use **_Local Cli Tool_** described on [cli-tool](https://taf.seli.wh.rnd.internal.ericsson.com/cli-tool/) documentation instead
as this tool is no longer supported.

## What is Local?

Local is the host you are running on.

## What is the TAF Local Command Executor?

The TAF local command executor is an API which allows you to execute commands on the machine the JVM is running on.

## How to get started

This API just has the default constructor so start with that

```java
    LocalCommandExecutor executor = new LocalCommandExecutor();
```java

It has 2 overloaded methods to execute commands. Both are overloaded in the same way, they either take a string with the command and
arguments in one string or they take a string with the command and a string array with all the arguments. The main difference is in
what they return, execute returns a boolean for success or failure and simpleExec returns a string which contains the response of the
command. The stdout, stderr and exit code from the command are also available using methods getStdOut() getStdErr() getExitCode().

```java
    String responseA = executor.simpleExec("ls -al");
    String responseB = executor.simpleExec("ls", "-a", "l");

    Boolean successA = executor.execute("ls -al");
    Boolean successB = executor.execute("ls", "-a", "l");

    String stdOut= executor.getStdOut();
    String stdErr= executor.getStdErr();
    int exitCode= executor.getExitCode();
```java

## API Documentation Link

[Local Command Executor API Documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/handlers/implementation/LocalCommandExecutor.html)
