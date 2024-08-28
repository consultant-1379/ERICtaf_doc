<head>
   <title>Out of Memory PerGem Exception</title>
</head>

# What to do if you get an Out of Memory PermGem Exception when running TAF

Depending on circumstances when running TAF with the default settings of the MVN JVM, it can happen that an Out Of Memory
PermGen exception occurs.

The solution is to set MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m" or similar values as an environment variable

## To set MAVEN_OPTS in Windows:

    1. Right click on My Computer and select Properties

    2. Click the Advanced System Settings link located in the left navigation of System Properties to display the Advanced System Properties

    3. Go to the Advanced tab and click the Environment Variables button located at the bottom of the Advanced System Properties configuration window

    4. Create a New user variable, set the Variable name to MAVEN_OPTS and set the Variable value to -Xmx1024m (or more)

```
Open a new command window and run mvn as normal.
```

## To set MAVEN_OPTS in Linux:

1. Add the following to your .bashrc file:-
```
	export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"
```

Open a terminal and run maven as normal.
