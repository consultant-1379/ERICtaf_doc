<head>
   <title>Remote Object Handler</title>
</head>

# Remote Object Handler

## What is a remote object

A remote object is an object (usually a file or folder) that is stored on a remote system.

## What is the Remote Object Handler

RemoteObjectHandler allows the user to send a file or a folder to another host i.e. from your local machine to a remote machine,
from your remote machine to your local or transfer objects between two remote machines.
It uses an ssh connections to sFTP the object between the two systems.

## Getting Started

<span style="color:#ba3925;">Constructor</span>

Arguments   | Description
:---------- | :-----------
Host        | The remote host you want to interact with
User        | The user you want to connect to the host with (Optional).

<span style="color:#ba3925;">Example</span>

**Sending one file:**

```java
Host msHost = HostConfigurator.getMs();
RemoteObjectHandler remote = new RemoteObjectHandler(msHost);

// sending file from local host to remotehost
String localFileName = "FileToSend.txt";   //File should be present in src/main/resources folder
of your TAF project or else include the full filename and path.
String remoteFileLocation = "/opt/resources/target/";    //Remember to use unix addresses if the
remote machine is a unix system
remote.copyLocalFileToRemote(localFileName ,remoteFileLocation);
```java

```java
// sending file from remote host to local host
String localFileLocation = "C:/src/resources/FilesToSend.txt";
String remoteFileLocation = "/opt/resources/target/FilesToSend.txt";
remote.copyRemoteFileToLocal(remoteFileLocation, localFileLocation);
```java

**Using a specific user**

```java
Host msHost = HostConfigurator.getMs();
User user = Host.getUser(UserType.ADMIN);
RemoteObjectHandler rfh = new RemoteObjectHandler(msHost, user);
```java

By default RemoteObjectHandler will use the default user from the host object, it is not guaranteed which user this will be. So if you have
multiple users in a host and you want you make sure your using a particular user, perhaps to ensure user has the required privileges
follow the example above.

Please note, if the file is packaged into a testware jar, using the FileFinder.findFile(String fileName) method will handle the path
to file regardless of location.

**Copying a directory to a remote location**

```java
Host msHost = HostConfigurator.getMs()
RemoteObjectHandler remote = new RemoteObjectHandler(msHost);

String remoteFileLocation = "/opt/resources/target/";    //Unix address if remote machine is unix
File dir = new File("src/main/FilesToSend");             //Local directory to be copied
remote.copyLocalDirToRemote(dir.getAbsolutePath(), remoteFileLocation);
```java

**Using RemoteObjectHandler with a private key**

Users can connect to machines that require public key authentication using remoteObjectHandler. Versions of all methods have been
added to the API to allow users to specify the private key on their local machine. These methods are the same as the original methods however
 they will have withSshKey in the method name and they now require the additional keyPath parameter. For example
 copyRemoteFileToLocal(remoteFile, localFile) is now available as copyRemoteFileToLocalWithSshKey(remoteFile, localFile, keyPath).

<span style="color:#ba3925;">Example</span>

**Sending one file using an SSH key:**

```java
Host host = HostConfigurator.getMs();
RemoteObjectHandler remote = new RemoteObjectHandler(host);

// sending file from local host to remotehost
String localFileName = "FileToSend.txt";
String remoteFileLocation = "/opt/resources/target/";
remote.copyLocalFileToRemoteWithSshKey(localFileName,remoteFileLocation, pathToKeyOnLocalMachine);

// sending file from remote host to local host
String localFileLocation = "C:/src/resources/FileReceived.txt";
String remoteFileLocation = "/opt/resources/target/FilesToSend.txt";
remote.copyRemoteFileToLocalWithSshKey(remoteFileLocation, localFileLocation, pathToKeyOnLocalMachine);
```java

<span style="color:#ba3925;">SFTP Failed to start Exception</span>

If while using the RemoteObjectHandler to send multiple files you receive an error stating that the SFTP client failed to start or the server has refused
the connection, it may be because the ssh configuration on the remote server has been set to only allow a certain number of concurrent ssh connections.
By default this value is 10. It will need to be changed to a higher value for this issue to be resolved.

1. Use putty connection to ssh to your remote machine

2. Go to the sshd configuration file cd /etc/ssh/sshd_config

3. Edit file and add “MaxSession &lt;your_new_value&gt;” to the file

4. Restart the SSH server by typing /etc/init.d/sshd restart

## API Documentation

[Remote Object Handler](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/tools/cli/handlers/impl/RemoteObjectHandler.html)
