<head>
   <title>Migrating from SshRemoteCommandExecutor (Deprecated) to CLICommandHelper</title>
</head>

# Migrating from SshRemoteCommandExecutor (Deprecated) to CLICommandHelper

## Instantiating the Classes

**<span style="color:#ba3925;">SshRemoteCommandExecutor (Depreciated):</span>**

* SshRemoteCommandExecutor executor = new SshRemoteCommandExecutor()

    * executor.setHost(Host host)

* SshRemoteCommandExecutor executor = new SshRemoteCommandExecutor(Host host)

If we take the above, SshRemoteCommandExecutor could have been created 2 ways. One which calls the default constructor and one which passes
the constructor a Host. Using the first method, meant that the host had to be set afterwards, whereas the second method initialized the
host on creation.

**<span style="color:#ba3925;">CLICommandHelper:</span>**

* <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper()</span>

    * <span style="color:#ba3925;">cmdHelper.createCliInstance(Host host)</span>

* <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper(Host host)</span>

    * or

* <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper(Host host, User username)</span>

With CLICommandHelper, you instantiate the class very similar to that of the old SshRemoteCommandExecutor. If you use the default constructor then
you must call "createCliInstance" method afterwards. Creating a CLI instance on a particular Host is identical to that of the SshRemoteCommandExecutor,
whereby you pass a host to the constructor. However CLICommandHelper has an additional constructor, whereby you specify the Host and the desired User
you wish to use on the host (e.g. new CLICommandHelper("sc1", "root"))

**Transition:**

* SshRemoteCommandExecutor executor = new SshRemoteCommandExecutor(); → <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper();</span>

    * cmdHelper.createCliInstance(Host host); → <span style="color:#ba3925;">cmdHelper.createCliInstance(Host host);</span>

* SshRemoteCommandExecutor executor = new SshRemoteCommandExecutor(Host host); → <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper(Host host);</span>

* (New) → <span style="color:#ba3925;">CLICommandHelper cmdHelper = new CLICommandHelper(Host host, User username)</span>

## Executing commands

<span style="color:#ba3925;">SshRemoteCommandExecutor:</span>

Contained four methods for executing commands:

* **_simpleExec(String cmdWithoutArgs, String… arguments)_** - Overloaded method takes two parameters. First parameter is the command to be executed.
  Second parameter is the arguments to pass to the command. Command output returned as String.

* **_execute(String cmdWithOutArgs, String… arguments)_** - Similar to "simpleExec(String cmdWithoutArgs, String… arguments)", however boolean value was
  returned indicating whether command executed without exception.

<span style="color:#ba3925;">CLICommandHelper:</span>

Encapsulates all of the above SshRemoteCommandExecutor methods into teo methods:

* <span style="color:#ba3925;">simpleExec(String… commands)</span> - Method which takes in a String array or a comma seperated list of commands.
  Allows for single or multiple command execution in one single mechanism.

* <span style="color:#ba3925;">simpleExec(Terminal terminal, String… commands)</span> - Similar to above method, but allows user to specify the
  terminal type in which to execute the command.

**Note:** simpleExec() method executed the command(s) once only, creating a new shell instance every time and closing the shell instance when completed
executing. This follows the same procedure that SshRemoteCommandExecutor used.

**Transition:**

Executing commands with the new CLICommandHelper carries minimal changes from that of SshRemoteCommandExecutor. However there are some small differences
that are worth noting:

* CLICommandHelper simpleExec() method allows single or multiple commands to be passed as a parameter.

    * <span style="color:#ba3925;">simpleExec("ps -ef | grep java");</span> Single command passed in as an argument

    * <span style="color:#ba3925;">simpleExec("ls -ltr", "cd /home/", "pwd", "locate term");</span> Comma seperated list of commands to be executed (Also can pass a String[] as argument to this method also)

* Commands that are passed to the new method must be in the format of the full command String. i.e. Command + args ("./var/opt/testScript.sh arg1 arg2")

* Unlike the old execute() method of the SshRemoteCommandHelper, exception checking is implemented as the command is getting executed. No need to check a command return
  value, as an exception would be thrown if command could not be executed successfully.

* Standard output is now returned as default, whereas with SshRemoteCommandExecutor this was provided as an optional parameter.

* Finally new simpleExec() method allows the user to specify the terminal type on which to execute the command(s) <span style="color:#ba3925;">simpleExec(Terminal.VT100, "ps -ef", "locate help", "rm -rf file");</span>



