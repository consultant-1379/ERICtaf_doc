<head>
   <title>Setting up your TAF project to log to PDUOSS Test Statistics Database</title>
</head>

# Setting up your TAF project to log to PDUOSS Test Statistics Database

## Logging to OSS TS Database

[Back to TOR_CI_Framework_Team wiki](http://atrclin2.athtem.eei.ericsson.se/wiki/index.php/TOR_CI_Framework_Team)

To enable a TAF Project to log to the PDU OSS Test Statistics database there are a number of steps to be followed:-

1. Add a logdb.properties file to the TAF project (typically in the 'src/main/resource' directory). Download the required file here
   [logdb-Tor.properties file](https://ericoll.internal.ericsson.com/sites/Integration_Verification/core_CI_Framework/Test%20Automation/logdb-Tor.properties)

2. Pass a name for the test run as a vm argument: '-Dname=&lt;a test name of your choice&gt;' **Note:** This is the name given to the test run in the TS database

3. Pass the location of the 'logdb.properties' file as a vm argument: '-Dlogdb.configuration=&ltpath to logdb.properties file at run time&gt'

4. Enable the TS database logwriter by passing a vm argument: '-Dlogwriters=se.ericsson.jcat.fw.ng.logging.writers.DbLogWriterRIPNG'

**Note:** If you are running your tests from a LITP installation in the ITTE network you will need to carry out the following additional steps

1. DNS setup: Make sure your node as a DNS set up correctly

```
# vi ./network-scripts/ifcfg-eth1
```

and add update the DNS entry to DNS1=``159.107.173.3''

2. Pass the following additional VM arguments at run time

```
-Dhttp.proxyHost=atproxy1.athtem.eei.ericsson.se -Dhttp.proxyPort=3128
```

To check that your project is logging to the TS database, please check the following link - http://eta-teststatistics.rnd.ki.sw.ericsson.se/ts/LatestTests.jsp
