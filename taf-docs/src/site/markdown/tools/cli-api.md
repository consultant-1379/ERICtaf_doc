<head>
   <title>What is Command-line interface (CLI)</title>
</head>

# What is Command-line interface (CLI)

**Note:** It is not recommended to use CLI API directly. Please use convenience tool [CLI (Tool)](https://taf.seli.wh.rnd.internal.ericsson.com/cli-tool) instead.
A command-line interface (CLI), also known as command-line user interface, console user interface, is a means of interacting with a computer program
where the user issues commands to the program in the form of successive lines of text (command lines). The CLI was the primary means of interaction
with most computer systems until the introduction of the video display terminal in the mid-1960s, and continued to be used on Unix and Linux systems.
The interface is usually implemented with a command line shell, which is a program that accepts commands as text input and converts commands to
appropriate operating system functions.

Operating system (OS) command line interfaces are usually distinct programs supplied with the operating system. A program that implements such a
text interface is often called a command-line interpreter, command processor or shell. The term shell, often used to describe a command-line
interpreter, can be in principle any program that constitutes the user-interface, including fully graphically oriented ones.

[Command-line_interface](https://en.wikipedia.org/wiki/Command-line_interface)

## What is TAF CLI API

**NOTE:** Current tool will be deprecated in future. We recommend start using new [CLI (Tool)](https://taf.seli.wh.rnd.internal.ericsson.com/cli-tool).

TAF CLI API is abstraction layer over a pure Java implementation of SSH2. CLI API is abstraction layer integrate allowing CLI functionality into
your own TAF testware. CLI API split the concrete realization of its CLI API and interface that is used into your own TAF testware.

Now the TAF have only one CLI API realization, based on [JSch - Java Secure Channel](http://www.jcraft.com/jsch/). But you able add into TAF or
into your TAF testware another, your owned CLI API realization.

## How to get Started

### <span style="color:#ba3925;">1. Create a test Maven project:</span>

Create a TAF-based test project

All needed TAF CLI API classes will be available to you out of the box.

### <span style="color:#ba3925;">2. Create Host Properties File</span>

Host Properties File Structure and Example

### <span style="color:#ba3925;">3. Execute the command using the CLI tool</span>

1. Get Host object which corresponds your hosts defined in host properties :
   <span style="color:#ba3925;">Host host = DataHandler.getHostByName("your-host-name");</span>
   or use other methods in [DataHandler](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/data/DataHandler.html)

2. Create CLI object, associated with your host:
   <span style="color:#ba3925;">CLI cli = new CLI(host);</span>

3. Execute the Command:
   <span style="color:#ba3925;">Shell shell = cli.executeCommand(Terminal.VT100, calcCommand);</span>

4. Use the expect method to wait for certain string (or) read method to read the output
   <span style="color:#ba3925;">shell.expect("Hello World!");</span>
   (or) <span style="color:#ba3925;">output=shell.read();</span>

5. Use writeln method to answer the queries prompted by the command
   <span style="color:#ba3925;">shell.writeln("2.0.10");</span>

6. Use getExitValue() to get the exit value of the command
   <span style="color:#ba3925;">int exitCode=shell.getExitValue();</span>

:-----  | :------
**TIP** | In case of long running command, use the read() method inside the while loop as below:

```java
    while(!shell.isClosed()){
      output=shell.read();
      logger.debug(output);
    }
```java

## API Documentation

TAF CLI API interfaces are part of the CLI-Tool module. Their description can be found in [CLI-Tool documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/package-frame.html) in package  <span style="color:#ba3925;">com.ericsson.cifwk.taf.tools.cli</span>

Most usable CLI APIs are

1. [<span style="color:#ba3925;">CLI</span> class](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/CLI.html) The main class of CLI Tool which provides a access to the shell and enables the commands execution

2. [<span style="color:#ba3925;">Shell</span> interface](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/Shell.html) This interface is used to describe a interaction with the shell process to read and write to it.

3. [<span style="color:#ba3925;">CLIOperator</span> class](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/CLIOperator.html) This class provides helper methods for executing the commands using cli openshell api.

:-----  | :------
**TIP** | For executing multiple commands in a single shell,use the CLIOperator instead of the CLI tool.

## Examples

<!--An example class showing the basic usage of the TAF CLI functionality can be seen here
Sample CLI class-->

Detailed CLI Examples can be found in GIT at the below location:
&lt;signum&gt;
<span style="color:#ba3925;">Git Repo:</span> ssh://&lt;signum&gt;@gerrit.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_examples

<span style="color:#ba3925;">Path to CLI Example:</span> /cli-example

Please refer [CLI Examples](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/TAF/CLI+example) for the description of the examples

