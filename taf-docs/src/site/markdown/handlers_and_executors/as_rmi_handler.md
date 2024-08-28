<head>
   <title>AS RMI Handler</title>
</head>

# AS RMI Handler

## What is AS RMI?

RMI is a mechanism used by an Application Server to allow remote clients to call methods on beans deployed inside the Application
Server Container.

## What is the TAF AS RMI Handler?

RMI Handler supports the fetching of beans/remote objects with a remote interface. It does this by using a JNDI lookup inside the
Application Server so the developer can then call methods on the bean.

## How to get started

Constructor of AsRmiHandler is called following:

```java
    AsRmiHandler(Host jbossNode)
```java

:------- | :--------
**NOTE** | Host requires to have RMI port set and application realm user.

This handler allows use of it to get bean/remote object using method getServiceViaJndiLookup():

```java
    Object rmiHandlerInstance.getServiceViaJndiLookup(String jndiString)
```java

You can then cast the return object to the type of the bean and call methods on it.

The Handler supports lookup of deployed versions of bean/services using getServiceVersion

```java
    List<String> getServiceVersion(String serviceNamePattern)
```java

This returns a list of versions for deployed service. The service version is resolved by a service name regular expression. e.g. mediationservice

:------- | :--------
**NOTE** | AsRmiHandler provides close method to close lookup context and connection to Application Server and these need to be used to gracefully close connections to AS.

```java
    void close();
```java

## Current Functionality

Fetch bean exposed with remote interface

## Known issues and workarounds

Change of Exception handling for close method

From version 2.3.49 of TAF onwards, the exception handling for the close method is now handled inside the AsRmiHandler.

**WARNING:** *This is a non-backward compatible change*

When you update to, or past, this version you will get a compilation error, remove the try/catch block and your code will work as before.



