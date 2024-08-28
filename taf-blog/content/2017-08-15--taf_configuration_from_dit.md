Title: TAF Hosts Configuration from DIT
Slug: taf-configuration-from-dit
Date: 2017-08-15 16:05
Category: TAF
Tags: taf, hosts, configuration
Authors: Mihails Volkovs
Status: published
Summary: TAF Update on configuring your testware (particularly hosts IP and IPv6) by external system - Deployment Inventory Tool (DIT).


# New TAF Configuration source

Since version 2.35.3 TAF supports another configuration source - **Deployment Inventory Tool (DIT)**.

The list of supported configuration sources (in activation order):

Configuration               | Description
--------------------------- | -------------------------------------------------------------------
Runtime Configuration       | Runtime Configurations, which allows setting properties in runtime.
System Configuration        | Configurations found in System.getProperties().
Environment Configuration   | Configurations found in System.getEnv().
User-defined Configuration  | Configurations residing in <USER_HOME>/taf_properties directory.
HTTP Configuration          | Configurations retrieved by HTTP.
DIT Configuration           | Configurations retrieved from Deployment Inventory Tool (DIT).
File System Configuration   | Scans for taf_properties directory in the classpath.
Classpath Configuration     | Scans inside jar files in the current classpath.

Items in the top will take preference over items in the bottom (if the same key is used).

# DIT Configuration activation

Configuration retrieval is activated by providing deployment name via property `taf.config.dit.deployment.name`.

Example:
```
mvn test -Dtaf.config.dit.deployment.name=ieatenmpd201-1
```

# Received Hosts example

Configuration from DIT defines hosts (name, type, external IP and/or external IPv6).

E.g. for deployment `ieatenmpd201-1` DIT returned following hosts:

Hostname | Type | IPv4 | IPv6
---------|------|------|------
lvsrouter_1 | lvsrouter | 131.160.202.25 | 2001:1b70:6207:0027:0000:0874:1001:0010
lvsrouter_2 | lvsrouter | 131.160.202.26 | 2001:1b70:6207:0027:0000:0874:1001:0011
haproxy-sb | haproxy-sb | 131.160.202.22 | 2001:1b70:6207:0027:0000:0874:1001:0006
svc_FM_vip | svc_FM_vip | 131.160.202.13 | 2001:1b70:6207:0027:0000:0874:1001:0049
mspm_5 | mspm | null | 2001:1b70:6207:0027:0000:0874:1001:0035
msnetlog_2 | msnetlog | null | 2001:1b70:6207:0027:0000:0874:1001:0030
mspm_4 | mspm | null | 2001:1b70:6207:0027:0000:0874:1001:0034
visinamingsb | visinamingsb | 131.160.202.16 | 2001:1b70:6207:0027:0000:0874:1001:0052
mspm_1 | mspm | null | 2001:1b70:6207:0027:0000:0874:1001:0031
mspm_3 | mspm | null | 2001:1b70:6207:0027:0000:0874:1001:0033
mspm_2 | mspm | null | 2001:1b70:6207:0027:0000:0874:1001:0032
mspmip_1 | mspmip | null | 2001:1b70:6207:0027:0000:0874:1001:0036
mspmip_2 | mspmip | null | 2001:1b70:6207:0027:0000:0874:1001:0037
msnetlog_1 | msnetlog | null | 2001:1b70:6207:0027:0000:0874:1001:0029
haproxy_1 | haproxy | 131.160.202.10 | 2001:1b70:6207:0027:0000:0874:1001:0007
nbalarmirp_2 | nbalarmirp | 131.160.202.28 | 2001:1b70:6207:0027:0000:0874:1001:0045
nbalarmirp_1 | nbalarmirp | 131.160.202.27 | 2001:1b70:6207:0027:0000:0874:1001:0044
scripting_2 | scripting | 131.160.202.18 | 2001:1b70:6207:0027:0000:0874:1001:0047
scripting_1 | scripting | 131.160.202.17 | 2001:1b70:6207:0027:0000:0874:1001:0046
emp_1 | emp | 131.160.202.21 | null
nfs | nfs | 131.160.202.32 | null
visinamingnb | visinamingnb | 131.160.202.15 | 2001:1b70:6207:0027:0000:0874:1001:0051
mscm_2 | mscm | null | 2001:1b70:6207:0027:0000:0874:1001:0016
mscm_1 | mscm | null | 2001:1b70:6207:0027:0000:0874:1001:0014
mscmip_1 | mscmip | null | 2001:1b70:6207:0027:0000:0874:1001:0023
msap_1 | msap | null | 2001:1b70:6207:0027:0000:0874:1001:0012
mscm_4 | mscm | null | 2001:1b70:6207:0027:0000:0874:1001:0018
mscmip_2 | mscmip | null | 2001:1b70:6207:0027:0000:0874:1001:0024
mscm_3 | mscm | null | 2001:1b70:6207:0027:0000:0874:1001:0017
msap_2 | msap | null | 2001:1b70:6207:0027:0000:0874:1001:0013
svc_CM_vip | svc_CM_vip | 131.160.202.12 | 2001:1b70:6207:0027:0000:0874:1001:0048
itservices_1 | itservices | 131.160.202.23 | 2001:1b70:6207:0027:0000:0874:1001:0008
itservices_2 | itservices | 131.160.202.24 | 2001:1b70:6207:0027:0000:0874:1001:0009
mscmce_4 | mscmce | null | 2001:1b70:6207:0027:0000:0874:1001:0022
mscmce_3 | mscmce | null | 2001:1b70:6207:0027:0000:0874:1001:0021
mscmce_2 | mscmce | null | 2001:1b70:6207:0027:0000:0874:1001:0020
mscmce_1 | mscmce | null | 2001:1b70:6207:0027:0000:0874:1001:0019
mssnmpfm_4 | mssnmpfm | null | 2001:1b70:6207:0027:0000:0874:1001:0043
esmon | esmon | 131.160.202.11 | null
msfm_1 | msfm | null | 2001:1b70:6207:0027:0000:0874:1001:0025
svc_PM_vip | svc_PM_vip | 131.160.202.14 | 2001:1b70:6207:0027:0000:0874:1001:0050
msfm_2 | msfm | null | 2001:1b70:6207:0027:0000:0874:1001:0026
msfm_3 | msfm | null | 2001:1b70:6207:0027:0000:0874:1001:0027
msfm_4 | msfm | null | 2001:1b70:6207:0027:0000:0874:1001:0028
elementmanager_2 | elementmanager | 131.160.202.20 | 2001:1b70:6207:0027:0000:0874:1001:0005
mssnmpfm_2 | mssnmpfm | null | 2001:1b70:6207:0027:0000:0874:1001:0041
enm_laf_1 | enm_laf | 131.160.202.39 | null
mssnmpfm_3 | mssnmpfm | null | 2001:1b70:6207:0027:0000:0874:1001:0042
elementmanager_1 | elementmanager | 131.160.202.19 | 2001:1b70:6207:0027:0000:0874:1001:0004
mssnmpfm_1 | mssnmpfm | null | 2001:1b70:6207:0027:0000:0874:1001:0040
mssnmpcm_1 | mssnmpcm | null | 2001:1b70:6207:0027:0000:0874:1001:0038
mssnmpcm_2 | mssnmpcm | null | 2001:1b70:6207:0027:0000:0874:1001:0039
amos_1 | amos | 131.160.202.29 | 2001:1b70:6207:0027:0000:0874:1001:0002
amos_2 | amos | 131.160.202.30 | 2001:1b70:6207:0027:0000:0874:1001:0003 |



Additionally deployment private key from DIT will be available in TAF Configuration (`taf.config.dit.deployment.private.key` property).

# Requests to DIT

Once DIT Configuration is activated TAF will send following requests (on first access to TAF Configuration, assuming deployment name is 'ieatenmpd201-1' and default URLs are used):

- http://atvts3395.athtem.eei.ericsson.se:3000/api/deployments?q=name=ieatenmpd201-1
- http://atvts3395.athtem.eei.ericsson.se:3000/api/documents/592c8cda82ee3e0020557633 (assuming first response returns sed_id = '592c8cda82ee3e0020557633')

It is possible to override DIT system location and request URLs by defining following properties:

Parameter              | Default value                                    | Property key
---------------------- | ------------------------------------------------ | -------------------------
Base URL               | `http://atvts3395.athtem.eei.ericsson.se:3000/api` | `taf.config.dit.url`
Deployment URL pattern | `/deployments?q=name=%s`                           | `taf.config.dit.deployment.url.pattern`
Sed URL pattern        | `/documents/%s`                                    | `taf.config.dit.sed.url.pattern`