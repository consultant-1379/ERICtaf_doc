<head>
    <title>Metrics Builder</title>
</head>

# Metrics Builder

##Overview

Metrics Builder is built on AMQP client, as a convenient tool for creating metrics messages with desired format and send to configured Message Bus.

All messages sent by the Metrics Builder are consumed by our java agent and persisted to the MySQL database, where they can be accessed by Saiku.

##Where can I get Metrics Builder?

Metrics Builder is a module in the [taf-tor-operators](https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.nms/taf-tor-operators) project

    <dependency>
        <groupId>com.ericsson.nms</groupId>
        <artifactId>EnmBase</artifactId>
        <version>please enter latest version</version>
    </dependency>

##Prerequisites for using Metrics Builder

To utilise the functionality provided by Metrics builder, you must have the following

* Message Bus
* Performance vApp with Agent configured to consume the messages and populate to MySQL database for Saiku usage

##How do I configure Metrics Builder?

Configuration for metrics should be specified in a either

* metrics.properties in /resources/taf_properties in the testware module (CXP... module)
* If run using TAF Executor, metrics properties can be specified in the additional properties field of the trigger configuration

Example Properties File

    taf.performance.metrics.amqp.host=atclvm793
    taf.performance.metrics.amqp.host.user=root
    taf.performance.metrics.amqp.host.password=shroot
    taf.performance.metrics.amqp.exchange=taf.assure.samples3

AMQP / MB host should be specified in your host properties

##Correlation between Metrics Builder and Saiku

Below is a table of what the keys used in Metrics Builder map to when visualizing the metrics in Saiku

| Metrics builder field | Saiku equivalent |
|-----------------------|:----------------:|
| Job Id                | Thread Id        |
| Execution Id          | Execution Id     |
| At date               | Event time       |
| Command               | Request Type     |
| Elements not copied   | Response code    |
| Execution Time        | Response time    |
| No of elements        | Response Size    |

##Using Metrics Builder

For example on using metrics builder see [Using Metrics Builder](using_metrics_builder.html)
