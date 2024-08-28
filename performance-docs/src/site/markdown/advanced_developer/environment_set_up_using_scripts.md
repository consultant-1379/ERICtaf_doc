<head>
    <title>Environment Setup Using Scripts</title>
</head>

#How to Manually Install Graphite/Saiku

To use TAF performance, Graphite, Grafana and Saiku need to be installed on a vApp or on 1/2 standalone VM’s.

**Note** These scripts can only be used on stand alone vms or physical systems

**Note** These steps should only be used by advanced users who have specific needs not met by the vApp template.

##Preparation for installation vApp/VM’s
Installation vApp must meet the following criteria:

* CentOS 6.5 or 6.6
* (vApp) Ability to ssh to host from gateway
* Connection to internet
* Script downloaded on all hosts
* Hosts should have free disk space/memory

Each ENM POD area has bare bones template saved in either the following catalogues: * DE_TAF * Oceans

These templates do not expire and are ready to go.
##Installation on standalone VM’s

Graphite/Grafana/Saiku can be installed as follows:

* All on 1 VM  or
* 2 VMs
    * Saiku on 1 and
    * Graphite and Grafana on the other.

Regardless of which configuration you choose the setup is the same.

Download the tar file from nexus: [saiku-graphite.tar](https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/com/ericsson/cifwk/saiku-graphite/1.0.9/saiku-graphite-1.0.9.tar) this needs to be completed on all VMs:


**To install Graphite/Grafana run the following steps**

* Copy tar to the host in question.
* Run the following commands:
    1. tar -pxvf saiku-graphite-1.0.9.tar -c /tmp
    2. sh /tmp/TAF_Performance/instgraph.sh with 1 parameter. - The name of the exchange you would like to use
        * example, sh /tmp/TAF_Performance/instgraph.sh taf.performance.graphite.teamName.feature
    3. Installation is now complete please verify

To access Graphite go to \<hostname\>.athtem.eei.ericsson.se:8081

To access Grafana go to \<hostname\>.athtem.eei.ericsson.se:8084



**To install Saiku run the following steps**

If installing Saiku on same VM as Graphite please skip step 1.

* Run the following commands:
    1. tar -pxvf saiku-graphite-1.0.9.tar -c /tmp
    2. sh /tmp/TAF_Performance/instSaiku.sh with 1 parameter. - The name of the exchange you would like to use
        * example, sh /tmp/TAF_Performance/instSaiku.sh taf.performance.saiku.teamName.feature
    3. Installation is now complete please verify

To access Saiku go to \<hostname\>.athtem.eei.ericsson.se:8082/saiku-ui
