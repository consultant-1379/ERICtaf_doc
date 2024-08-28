<head>
   <title>Logging</title>
</head>

# Logging

## How to use logging

**Logging:**

Logging is used for writing messages during runtime. It can provide information about statistics, performance or any other information
available at runtime. Logging allows for logging levels. It can be set to only log information in certain situations, for example in the
event of a fatal error.

The use of System.out for logging is not recommended by convention. Using System.out for debugging purposes wastes resources, clutters
code and affects readability. System.out prints all the time if enabled, whereas logging can be turned off and on as required at the class
or package level, without searching through code for individual entries.

**Which logging library to use:**

TAF recommends using SLF4J logging.

SLF4J is a facade which allows end users to plug in their own preferred logging system in at runtime with minimal effort.

## Levels

**What level to use:**

The level used depends on the task you are working on. For example, during development an application can be set to DEBUG level logging,
allowing for more information about the application to be logged. During normal activity the application can then be set to ERROR, where
only potentially harmful events will be logged.

There are 7 levels and each level includes all higher priority levels.

* **OFF** - Disables logging.

* **TRACE** - The TRACE level designates finer-grained informational events than DEBUG.

* **DEBUG** - The DEBUG Level designates fine-grained informational events that are most useful to debug an application.

* **INFO** - The INFO level designates informational messages that highlight the progress of the application at coarse-grained level.

* **WARN** - The WARN level designates potentially harmful situations.

* **ERROR** - The ERROR level designates error events that might still allow the application to continue running.

* **ALL** - Enables all logging levels

## Configuration

**How to enable/disable specific levels:**

Logging levels are set in the Log4J properties file. These can be set at package or class level.
The log4j properties file should ideally be put it in src/main/resources of the operators module, then the Ericmodule_CXPxxxxxxx module as
next preference.

In TAF, WARN level logging is set by default. This can be changed by changing rootlogger setting in the log4j properties file. (See example below)

:------- | :----------------
**NOTE** | _Specifying logging levels at runtime_. Logging levels can also be set at runtime by specifying another Log4J properties file as a -D parameter. For example: mvn test -Dlog4j.configuration=file:C:\Users\log4jfiles\log4j.properties.

**Appenders:**

Log4J uses appenders to specify what is to be done with the logging output. Some possible uses are to send the logging to console, to save to a file or to
append to another logging session. For example, log4j.appender.A1=org.apache.log4j.ConsoleAppender.

By default TAF appends logging output to console.

**What variable name to give the instance of the logger:**

TAF recommends a simple and descriptive variable name e.g. LOG or LOGGER. Logger should always be instantiated as static and final. Therefore variable names
should always be capitalized.

## Examples

**Example LOG4J properties file:**

```
# <Level> is a valid log4j level
# Level DEBUG - The DEBUG Level designates fine-grained informational events that are most useful to debug an application.
# Level INFO - The INFO level designates informational messages that highlight the progress of the application at coarse-grained level.
# Level WARN - The WARN level designates potentially harmful situations.
# Level ERROR - The ERROR level designates error events that might still allow the application to continue running.
# Level FATAL - The FATAL level designates very severe error events that will presumably lead the application to abort.

# In addition, there are two special levels of logging available: (descriptions borrowed from the log4j API http://jakarta.apache.org/log4j/docs/api/index.html):

# Level ALL -The ALL Level has the lowest possible rank and is intended to turn on all logging.
# Level OFF - The OFF Level has the highest possible rank and is intended to turn off logging.
log4j.rootLogger=WARN, A1

# Special case for the ENMTestCaseListener to have log level set to DEBUG.
log4j.logger.com.ericsson.cifwk.taf.execution.ENMTestCaseListener=DEBUG

# A1 is set to be a ConsoleAppender.
log4j.appender.A1=org.apache.log4j.ConsoleAppender
# A1 uses PatternLayout.
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=%d{ISO8601} [%t] [%p] [%c] %m\n
# need to set follow = true for the appender as it gets moved around in junit tests
log4j.appender.A1.follow=true
```

**Example class using logging:**

```
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 public class ExampleLogger {

   final static Logger LOG = LoggerFactory.getLogger(ExampleLogger.class);
   int number;
   int oldNumber;

   public void setNumber(int newNumber) {
     oldNumber = number;
     number = newNumber;
     LOG.info("Number set to {}. Old number was {}.", number, oldNumber);
     if(number > 400) {
       LOG.warn("New number is greater than 400.");
     }
   }
 }
```

## More information

More information is available from http://www.slf4j.org/. The SLF4J online manual is available at http://www.slf4j.org/manual.html
