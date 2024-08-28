<head>
   <title>JMS Handler</title>
</head>

# JMS Handler

## What is JMS?

JMS is the Java Messaging Service. More info can be found at http://en.wikipedia.org/wiki/Java_Message_Service

## What is the TAF JMS Handler?

The TAF JMS Handler is an API which abstracts the details of connecting to JMS on an instance of JBOSS.

Once an instance of the JMS Handler is created it starts listening to the destination specified and stores all messages from that destination
that can then be used by the developer.

Note: It would not be recommended to listen to destinations with huge volumes of messages for a prolonged period as this may cause out of memory
issues on the TAF JVM if too many messages require to be stored.

## How to get started

Firstly you need to create an instance of the JMSHandler

```java
    JMSHandler jms = new JMSHandler(Host, destination);
```java

:------- | :---------
**NOTE** | Please note the host object needs to specify RMI port and Application Realm user

The host here is the jboss instance and the destination is the queue/topic which you want to read messages from.

The constructor also has 4 optional arguments. In order they are:

argument            | description                                                                                                                           | default
:-----------------  | :----------------------------------------------------------------------------------------------------------------------               | :-------------------------------
transacted          | indicates whether the session is transacted or not.                                                                                    | false
acknowledgementMode | indicates whether the client will acknowledge messages it receives                                                                    | Client Acknowledge
consumeMessages     | indicates whether this instance of JMSHandler will take the messages it receives off the queue.                                       | false
producerOnly        | indicates whether this instance of JMSHandler will not read messages from the destination but can send messages to the destination.   | false
messageWaitTimeOut  | indicates and can be set that the timeout to receive messages from jms is a set period of time                                        | 1000

To search for messages from a specific destination call the following method passing 2 arguments. First the message content to look for and secondly the time to wait before timing out,
this throws an exception which you must catch. The default time is 10 seconds.

```java
    jms.getMessageFromJms(messageContent, timeout);
```java

Once you are finished listening please close the JMS Handler. This is imperative as if you create a new instance of JMS Handler on the same destination you will have no way of
knowing which handler will get which messages from the destination.

```java
    jms.close();
```java

The JMSHandler is continuously storing messages once it is instantiated. If the list gets too big you can clear it yourself.

```java
    BlockingQueue jms.getAllMessages();
    BlockingQueue jms.getUndeliveredMessages();

    jms.getAllMessages().clear();
    jms.getUndeliveredMessages.clear();
```java

You can also asynchronously listen for messages with the JMSHandler. There are a number of steps involved.

1. Create a class which implements MessageListener.

2. Implement the onMessage method which will execute when a message is found.

3. Register the Message Listener with JMS.

```java
    jms.listen(Object expectedMessageContent,MessageListener messageListenerImplementation)
```java

It is also possible to send messages to the JMS Destination.

```java
    jms.sendObjectMessage(object)
    jms.sendTextMessage(string)
```java

## Best use of JMSHandler

JMSHandler works best when consuming messages in AUTO_ACKNOWLEDGE mode.

```java
    Host host = DataHandler.getHostByName("sc1");
    String destination = "jms/queue/test" // Whatever has been setup in your JBOSS instance

    JmsHandler jms = new JmsHandler(node,destination,false,Session.AUTO_ACKNOWLEDGE,true,false);

    List allMessages = jms.getAllMessages().toList();
    //or
    Message m = jms.getMessageFromJms("whatever Object is in the message");
```java

AS a Producer, sending messages to jms using JMSHandler the CLIENT_ACKNOWLEDGE mode is best to use.

```java
    JmsHandler jms = new JmsHandler(node,destination,false,Session.CLIENT_ACKNOWLEDGE,false,true)

    jms.sendObjectMessage(Object whatever);
    jms.sendTexttMessage("Hello World");

    //other examples
    Byte byteMessage = "Message to send"..getBytes();
    jms.sendByteMessage(byteMessage); //convert message to bytes first

    jms.sendMapMessage(Map mappedObject);
```java

## API Documentation Link

[JMS Handler API Documentation](https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/handlers/jms/JmsHandler.html)

## Current Functionality

Create an instance of JMSHandler and wait for a messages, synchronously and asynchronously. Supported message types are TextMessage and ObjectMessage

Send Object and Text messages to destination.

## Known issues and workarounds

Leaving jms handler instance not closed, will occur in warning from garbage collector closing the connection.



