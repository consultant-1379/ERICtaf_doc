<head>
   <title>What is CLICommandHelper?</title>
</head>

# What is CLICommandHelper?

**NOTE:** Current tool will be deprecated in future. We recommend start using new [CLI (Tool)](https://taf.seli.wh.rnd.internal.ericsson.com/cli-tool).

CLICommandHelper is a convenience class for the current TAF CLI Tool. The purpose of this class is to provide users with easier use and greater understanding of
our current CLI Tool, as well as implementing some features, which had not been originally included in the initial implementation.

The features that were added in CLICommandHelper allow users to:

* Get execution time of a command (On shell session only)

* Get exit value of a command (On shell session only)

* Create a shell instance on a null/blank terminal instance(Through extending CLICommandHelper.java only)

These new features, as well as all the initial CLI functionality, provide a greater CLI experience to the user.

To see all of the functionality that the CLICommandHelper provides and the underlying CLI implementation visit the javadoc links below:

* CLI: [CLI Javadoc] (https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/CLI.html)

* CLICommandHelper: [CLICommandHelper Javadoc] (https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/CLICommandHelper.html)

## Migrating from the SshRemoteCommandExecutor to the CLICommandHelper

As of TAF Version 2.0.2 (Current TAF version at time of writing 2.0.25), SshRemoteCommandExecutor has been replaced with our latest CLI Tool. CLICommandHelper is a
class built on top of our latest tool and exposes its' underlying functionality, as well as providing additional features itself.

For a detailed description on how to migrate from the SshRemoteCommandExecutor to the CLICommandHelper, visit the following link Migrating to CLICommandHelper

## Executing commands using simpleExec

This section will show how to execute a command using CLICommandHelpers simpleExec(String… commands) method.

It is aimed at every user of our CLI tool, in particular those who are migrating from SshRemoteCommandExecutor and new CLI users.

Like the simpleExec() method from SshRemoteCommandExecutor, the new method will:

* Create a new shell instance every time

* Execute the command(s) on the shell

* Return the standard output from command execution

* Close the shell instance when finished command execution.

**<span style="color:#ba3925;">Example:</span>**

```java
    CLICommandHelper cmdHelper = new CLICommandHelper("sc2")
    String response = cmdHelper.simpleExec("whoami", "du -h", "ps -ef | grep java") // Executes
    the string seperated commands as one
```java

## Creating a session and executing commands

The core functionality of TAFs' CLI tool lies within its' ability to allow a user to create a shell session, execute multiple commands,
get results from these commands and then close their created session.

CLICommandHelper encapsulates this functionality, allowing a shell session to be created and commands to be executed in a series
of simple steps. The common procedural steps to follow are:

**<span style="color:#ba3925;">1. Create Instance of CLICommandHelper</span>**

To create an instance of CLICommandHelper, simple create the CLICommandHandler in one of the following ways

* CLICommandHelper cmdHelper = new CLICommandHelper(Host host);

* CLICommandHelper cmdHelper = new CLICommandHelper(Host host, User unsername);

* CLICommandHelper cmdHelper = new CLICommandHelper();

       * cmdHelper.createInstance(Host host);

       * cmdHelper.createInstance(Host host, User username);

* Extend the CLICommandHelper class

**<span style="color:#ba3925;">2. Execute a command</span>**

To execute a command on the shell, simply call the following method:

* cmdHelper.execute(String command);

This method takes in a String respresentation of the command to execute. The command should take the form of a full command, including the
arguments, as one whole String. For example: cmdHelper.execute("locate term"), whereby "locate" is the command and "term" is the argument.

**<span style="color:#ba3925;">3. Retrieve standard output from previous command</span>**

To retrieve the standard output the following method(s) should be used:

* cmdHelper.getStdOut();

* cmdHelper.getStdOut(long timeoutInSeconds);

If you call cmdHelper.getStdOut() with no parameters, then the standard output is read after the default number of seconds (5). If
you pass a timeout parameter using cmdHelper.getStdOut(long timeoutInSeconds), the standard output is read after the specified number of
seconds. For example: cmdHelper.getStdOut(15) would read the output after 15 seconds

**<span style="color:#ba3925;">4. Get exit value of previous command</span>**

To return the exit value of a command execute:

* cmdHelper.getCommandExitValue();

As the method name suggests, this method will return the exit value of the previous command (as an **Integer**) If the command execution value could not be retrieve then an exception will be thrown.

**Note**: To use getCommandExitValue(), the getStdOut() method **must** be called before this method can be used. This is due to the fact that the command
execution value is retrieved from standard output, so it is imperative that the standard output is returned first.

**<span style="color:#ba3925;">5. Get execution time of previous command</span>**

To get the execution time for a command call the following method:

* cmdHelper.getCommandExecutionTime()

Returns and integer representation of the command execution time in milliseconds. Like getCommandExitValue() and exception will be thrown if the execution time cannot be retrieved.

**Note**: As with getCommandExitValue(), the getStdOut() method **must** be called before this method in order to get the execution time.

**<span style="color:#ba3925;">6. Close the shell instance</span>**

The functionality to close a shell instance is to be handled by the CLI user themselves, this can be achieved using the following command:

* cmdHelper.execute("exit");

Simply what this method does is close a shell instance by calling "exit" within that shell. The user has control over closing the shell instances.
This is because new shell processes can be created with a shell, allowing multiple shell openings inside the main shell. So the view was taken
that when the user opens "inside" shells, they should have the power to close them also.

**<span style="color:#ba3925;">7. Check that the shell has closed successfully</span>**

To ensure the the shell closed as expected we call the method(s):

* cmdHelper.expectShellClosure();

* cmdHelper.expectShellClosure(long timeoutInSeconds);

This method checks that the shell instance will close within a certain time period. If expectShellClose() is called then the shell will expect to be closed within
the default timeout of 5 seconds. Use expectShellClosure(long timeoutInSeconds) to define the time in which the shell should close. This method returns void
and its success is based on whether a timeout exception is thrown or not. (No timeout == success)

**Note**: Using this method, without closing a shell instance beforehand is redundant and will throw an exception. Ensure that the command to
close the shell has been executed <span style="color:#ba3925;">(cmdHelper.execute("exit");)</span>

**<span style="color:#ba3925;">8. Ensure that the shell is closed</span>**

To check that the shell is in fact closed, use the following:

* cmdHelper.isClosed();

The isClosed() method is used to check that the target process (shell instance in this case) is closed. This method returns true if the shell process closed successfully.

**Note**: This method is not just specific to a shell instance closing, but can also be used to check that a script process has closed successfully also.
For example a script could require various user inputs, when all of these inputs have been supplied, the script process would close and the isClosed()
method could be used to check this process is closed.

**<span style="color:#ba3925;">9. Check the exit value of the shell</span>**

Finally to retrieve the exit code from the shell:

* cmdHelper.getShellExitValue();

* cmdHelper.getShellExitValue(long timeoutInSeconds);

Method which returns an **Integer** value indicating the exit code of the finished process. Using getShellExitValue() will aim to retrieve the
exit value within the default timeout of 5 seconds. getShellExitValue(long timeoutInSeconds) will look to return the exit value within the
specified timeout value. Method will throw a timeout exception if exit code cannot be gotten.

## Complete Shell Example

For the example we will incorporate the steps mentioned above into one sequential flow. We will create a shell instance, execute a command,
return the ouputs and then close the shell session using the CLICommandHelper

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(host);
    cmdHelper.execute("/var/opt/ericsson/test/test.sh"); //Execute some script which echos output
    cmdHelper.getStdOut(); // Retrieve the standard output
    cmdHelper.getCommandExitValue();
    cmdHelper.getCommandExecutionTime();
    cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
```java

## Executing a script with user input required

There is many cases where users may want to execute a script which requires some sort of input. In this case these interactive scripts are treated as processes. For these processes to complete, the user must satisfy the required inputs required for that script.

For example, a shell script requires a user to input their personal details: Name, Age and Date of Birth. The script will look for a an input for each one of these details. After all three entries have been supplied to the script, that script process can then close.

In CLICommandHelper this script interaction is represented by the methods:

* runInteractiveScript(String script);

* interactWithShell(String input);

The methods should be used for the following purposes:

1. Executing a script which requires user input (runInteractiveScript()).

2. Inputting data as replies to the scripts prompts(interactWithShell()).

**<span style="color:#ba3925;">Interactive script example</span>**

This example will show a simple bash script ("test.sh"), which prompts the user for three inputs: Name, Age and Date of Birth.

```
#! /bin/bash
    read -p "Enter your name:"

    read -p "Enter your age:"

    read -p "Enter your date of birth:"
```

The script above will ask the user each of these questions, prompting the user for input for each.

To implement this using CLICommandHelper see the following code example:

```
    CLICommandHelper cmdHelper = new CLICommandHelper(host); // Create instance on sc2 host
    cmdHelper.openShell(); // Open a shell instance
    cmdHelper.runInteractiveScript("/opt/ericsson/scripts/test.sh"); // Execute the script test.sh
    cmdHelper.expect("Enter your name:"); // expect method which checks the standard output for
    prompt string
    cmdHelper.interactWithShell("Joe"); // Provide the input to the script prompt
    cmdHelper.expect("Enter your age:");
    cmdHelper.interactWithShell("28");
    cmdHelper.expect("Enter your date of birth");
    cmdHelper.interactWithShell("14-02-1986");
    cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
```

For more information on the additional CLICommandHelper functionality please see the API documentation link below:

[CLICommandHelper Javadoc] (https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/CLICommandHelper.html)

## HopBuilder

HopBuilder is a convenient inner class inside CliCommandHelper which contains a singular overloaded method "hop". This method allows the user to do the following on the shell:

* ssh onto a host with a particular user → <span style="color:#ba3925;">cmdHelper.newHopBuilder().hop(Host host, User user).build();</span>

* ssh onto a host with default host username → <span style="color:#ba3925;">cmdHelper.newHopBuilder().hop(Host host).build();</span>

* Switch user on current host → <span style="color:#ba3925;">cmdHelper.newHopBuilder().hop(User user).build();</span>

Below are examples of each hop method implemented:

* To ssh onto a host with a particular user:

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(Host host, User user).build();
    cmdHelper.execute("command to execute");
```java

Equivalent of executing: *ssh user@hostname* on the shell.

* To ssh onto a host with default host user:

Uses the user credentials defined for host (from host definition in properties) to ssh.

Equivalent of executing *ssh user@hostname* on the shell

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(Host host).build();
    cmdHelper.execute("command to execute");
```java

To switch user on current host:

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(User user).build();
    cmdHelper.execute("command to execute");
```java

Equivalent of executing: *su - user* on the shell

The above three examples show each implementation of the hop method that is available within HopBuilder. HopBuilder is built using
the builder design pattern, which means it is possible to chain multiple hop methods together and execute them as one.

For example, if we wanted to ssh onto a host as a certain user and then switch user on the new host:

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(Host host, User user).hop(User user2).build();
    cmdHelper.execute("command to execute");
```java

This is the equivalent of typing: *ssh user@hostname* followed by *su - user* on a shell.

**<span style="color:#ba3925;">StrictHostChecking</span>**

CLICommandHelper also has an api where the user may set a flag for *StrictHostChecking* to **TRUE** or **FALSE**

e.g. to turn *StrictHostChecking* **on**

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(Host host, User user, boolean true).hop(User user2).build();
```java

This is the equivalent of typing: *ssh -o StrictHostKeyChecking=yes user@hostname* followed by *su - user* on a shell.

or

to turn *StrictHostChecking* **off**

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(Host host);
    cmdHelper.newHopBuilder().hop(Host host, User user, boolean false).hop(User user2).build();
```java

This is the equivalent of typing: *ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no user@hostname* followed by *su - user* on a shell.

:------   | :----------
**NOTE**  | **<span style="color:#ba3925;">Execute commands after Hopping</span>** - If users need to execute additional commands after hopping, CliCommandHelper can simply be used as normal.

:--------    | :----------
**WARNING**  | **<span style="color:#ba3925;">Close down that session!!</span>** - It is vitally important that users call the disconnect() method of CliCommandHelper when finished hopping or finished executing commands on the shell. This is so the shell instances are closed and the shell session destroyed, otherwise resource leaks WILL occur.

:--------    | :----------
**WARNING**  | **<span style="color:#ba3925;">Do not use simpleExec() method in conjunction with newHopBuilder()</span>** - The cmdHelper.simpleExec() method should not be used in conjunction with cmdHelper.newHopBuilder() method, please use the cmdHelper.execute() method instead.

## CLICommandHelper: Select shell type

CLICommandHelper API now allows the user to change shell type that they are working with. Be it bash(default), tcsh, sh, ksh or csh.

There are two mechanisms provided to the user for setting the shell type:

* First mechansim is to pass shell type to CLICommandHelper constructor. This will open a shell with the user specified shell type.

Below is a code example of how this is achieved with CLICommandHelper:

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(host, ShellType.ksh);
```java

This will create a command helper instance and open up a shell with ksh(Korn shell) as the selected shell type.

:------   | :----------
**NOTE**  | If user needs to specify a particular User object then the following constructor should be used: *CLICommandHelper(Host host, User user, ShellType shellType)*.

* Second mechanism is to call the setShell(ShellType shellType) method in CLICommandHelper.

Code example below shows how to achieve this:

```java
    CLICommandHelper cmdHelper = new CLICommandHelper(host);
    cmdHelper.setShell(ShellType.tcsh);
```java

This will set the shell type to use after CLICommandHelper instance has been created.

:------   | :----------
**NOTE**  | ShellType is an Enum class which contains various well known shell types (Bash, tcsh, ksh, csh & sh). If a particular shell type needs to be added, please let the TAF team know and we can add this shell type to the Enum.

:------   | :----------
**NOTE**  | If you are using first mechanism mentioned above to select shell type, then there is no need to call openShell() method to open a shell instance. This is done in the background for the user whilst setting the shell type. Also if setShell() is called (Second mechanism) without openShell() being called previous, setShell() will open the shell instance in the background too.

## Best Practices

<span style="color:#ba3925;">cmdHelper.write()</span>

When using this method a new line character, "\n", must be concatenated to the string which is passed to the method. Not passing this
character is the same as writing a command in a terminal and walking away before hitting enter.

Example: cmdHelper.write(command+"\n");

<span style="color:#ba3925;">Long running commands</span>

If you have commands that may take a long time to complete then it is advisable to use cmdHelper.execute(command). This waits for the
command to complete so that it can store the exit code for this command.

<span style="color:#ba3925;">Terminal Type</span>

Depending on what terminal type you use the output of your commands will be formatted differently. By default vt100 is used but you can use vt100, xterm or null.

To set the terminal pass the preferred type to the openShell method.

Example: cmdHelper.openShell(Terminal.XTERM); Example: cmdHelper.opensHell(null);

Here is an example of the same command, ls, run using the 3 different options:

**null**

```
    70-persistent-net.rules
    anaconda-ks.cfg
    dhclient-exit-hooks
    ethomev_script.sh
    install.log
    install.log.syslog
    js.bsh
    ks-post.log
    RPM-GPG-KEY.dag.txt
    sleep_script.sh
    vSPC.py
```

**vt100**

```
    70-persistent-net.rules  install.log         RPM-GPG-KEY.dag.txt
    anaconda-ks.cfg          install.log.syslog  sleep_script.sh
    dhclient-exit-hooks      js.bsh              vSPC.py
    ethomev_script.sh        ks-post.log
    [root@atvts969 ~]#
```

**xterm**

```
    70-persistent-net.rules  install.log         RPM-GPG-KEY.dag.txt
    anaconda-ks.cfg          install.log.syslog  sleep_script.sh
    dhclient-exit-hooks      js.bsh              vSPC.py
    ethomev_script.sh        ks-post.log
    root@atvts969:~[root@atvts969 ~]#
```
