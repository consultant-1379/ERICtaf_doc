<head>
   <title>TAF Test Management - Introduction</title>
</head>

# Introduction

[TAF Test Management](http://taftm.lmera.ericsson.se) (TAF TM) serves the purpose of managing test cases, whilst ensuring all product requirements are being
covered by traceable test cases. It allows test case definition, metadata association, test case grouping and filtering.

TAF TM main characteristics:

* Web based (modern design - HTML5 + CSS2/3), using [UI-SDK](https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/sites/tor/uisdk/latest/index.html)
* Ericsson branded
* High Availability Application Server / Database, hosted in Linkoping HUB
* Automatic Backups
* Scalable Solution

TAF TM ensures test case traceability, assigning a global unique ID to each test case on its creation. Test case ID is linked to parent requirement in the
Requirement Management Tool (e.g. [JIRA](https://www.atlassian.com/software/jira)).

TAF TM has been designed using a modular approach and can be integrated into different Requirement Management Tools and IDEs via plug-ins. At the moment,
TAF TM is integrated with [PDU OSS JIRA](https://jira-oss.seli.wh.rnd.internal.ericsson.com) allowing linking of TAF TM test cases to JIRA requirements. Also, TAF TM is integrated
with Eclipse IDE, allowing the interaction with TAF Test Management on a test case level, giving the test developer the ability to document the test case while
developing it (see [TAF Eclipse plug-in](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/TAF/TAF+TM+Eclipse+Plugin) for more details).


Please follow link to documentation: [Test Management Documentation](https://taftm.seli.wh.rnd.internal.ericsson.com/#help/app/tm)
