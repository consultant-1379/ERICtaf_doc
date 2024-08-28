Title: Did you know you can now exclude rows of csv data without deleting them?
Slug: comment-out-csv-rows
Date: 2016-08-19 13:40
Category: TAF
Tags: best practices, new feature
Authors: Thomas Melville
Status: published
Summary: This blog will show you how to quickly exclude rows of csv data from being executed without having to delete them.

Currently you have to delete rows of data from your csv file if you want to exclude them from being executed in your test.

Well not from TAF version 2.29.1 onwards!

It is now possible to comment out a row of csv data by placing a # at the start of the line. For example:

```text
fTestCaseId,networkElementId,nodeType,port,transportType,secureUserName,secureUserPassword,ossPrefix,platformType,ossModelIdentity,rbac,cmEditSetAttributes
TC_0001,SGSN-16A-CP01-V101,SGSN-MME,22,SSH,netsim,netsim,,,,,active=true
#TC_0002,LTE08dg2ERBS00001,RadioNode,6513,TLS,netsim,netsim,,,,,active=true
#TC_0003,LTE01ERBS00002,ERBS,,,netsim,netsim,,CPP,16B-G.1.281,,active=true
TC_0004,K3C119301,MGW,,,netsim,netsim,,CPP,1484-383-806,,active=true,5,180
TC_0005,SGSN-16A-CP01-V102,SGSN-MME,22,SSH,netsim,netsim,,,,true,active=true
```

At runtime time only the first, fourth and fifth rows will be executed. 

It is also possible to change the character which is used to specify a comment. 
This is a global setting which will affect all the datasources in your project.
For example:

```properties
taf.data.comment.identifier=//
```

The above csv data will then become:

```text
fTestCaseId,networkElementId,nodeType,port,transportType,secureUserName,secureUserPassword,ossPrefix,platformType,ossModelIdentity,rbac,cmEditSetAttributes
TC_0001,SGSN-16A-CP01-V101,SGSN-MME,22,SSH,netsim,netsim,,,,,active=true
//TC_0002,LTE08dg2ERBS00001,RadioNode,6513,TLS,netsim,netsim,,,,,active=true
//TC_0003,LTE01ERBS00002,ERBS,,,netsim,netsim,,CPP,16B-G.1.281,,active=true
TC_0004,K3C119301,MGW,,,netsim,netsim,,CPP,1484-383-806,,active=true
TC_0005,SGSN-16A-CP01-V102,SGSN-MME,22,SSH,netsim,netsim,,,,true,active=true
```

*NOTE* The comment delimiter only has an affect at runtime in TAF execution. If you are editing the csv in excel the character is treated just like any other character and will be displayed in the first cell of the row.
