<head>
    <title>Environment Setup Using Puppet</title>
</head>

#How to Install Graphite/Saiku using puppet

Puppet scripts are used to install and configure each TAF Performance module component.

**Note** These steps should only be used by advanced users who have specific needs not met by the vApp template.

##Components

TAF Performance module consists of 4 components:

* Saiku - connects to a wide range of data sources. Shows historical performance data.
* Graphite - a real-time graphing system
* Grafana - metrics dashboard on top of Graphite. Real time tests performance monitoring.
* Collectd - is a daemon which collects system performance statistics periodically and provides mechanisms to store the values in a variety of ways. See Collectd in TAF

##Prerequisites

Using Puppet requires vApp with configured networking.  Requirements for vApp are following:

* CentOS 6.5 or 6.6
* Ability to ssh to host from gateway
* Connection to internet
* Hosts and gateway should not have litp puppet installation (i.e. `pgrep puppet` should return nothing)
* Hosts should have free disk space/memory (minimum 1GB free)

Each ENM POD area has bare bones template saved in either the following catalogues: * DE_TAF * Oceans

Puppet scripts should be available on gateway node. The easy way to achieve this - clone ERICTaf_te_puppet_performance repo to gateway.
Checkout Puppet Master

    cd ~
    git clone ssh://USERNAME@gerrit.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_te_puppet_performance
    cd ERICtaf_te_puppet_performance


##Configuration

This sections describes configuration steps prior installation.

**Configuring common properties**

All module properties are located in hiera_data/performance.yaml

Required properties to update:

* gateway_host
    * set property value to your gateway host address (e.g. gateway_host: atvtsXXX.athtem.eei.ericsson.se);
* te_saiku_version
    * set property value to latest TAF TE released version (e.g. te_saiku_version: 1.0.17);
* te_metrics_dwh_version
    * set property value to latest TAF TE released version (e.g. te_metrics_dwh_version: 1.0.17);
* \#IP section properties
    * there is no guarantee VM is available by the same IP on different vApp. IP section lets redefine ip adress for VMs where TAF TE components are going to be installed;
    * set ip_graphite property value to VMs ip where Graphite will be installed
    * set ip_saiku property value to VMs ip where Saiku will be installed
    * set ip_grafana property value to VMs ip where Grafana will be installed
    * set ip_elasticsearch property value to VMs ip where Elastic Search will be installed
    * set ip_rabbitmq proverty value to VMs ip where RabbitMq will be installed

        Example
        \#IP
        ip_graphite: 192.168.0.108                      #vApp Node IP with Graphite
        ip_saiku: 192.168.0.108                         #vApp Node IP with Saiku
        ip_grafana: 192.168.0.108                       #vApp Node IP with Grafana
        ip_elasticsearch: 192.168.0.108                 #vApp Node IP with Elastic search
        ip_rabbitmq: 192.168.0.200                      #vApp Node IP with Rabbit MQ installed
* \#PORTS section properties
    * used in routing and firewall configuration
    * by default port_rabbitmq is set to 5672 and port_rabbitmq_cli is set to 15672. Edit if required.

        Example
        #PORTS, used in routing and firewall configuration
        port_graphite: 8081
        port_saiku: 8082
        port_grafana: 8084
        port_elasticsearch: 9200
        port_rabbitmq: 5672
        port_rabbitmq_cli: 15672

    **Important**
        Make sure port is not used by other application.

##Configuring node definitions

Configure node definitions in manifests/site.pp file. For each node definition you can include Puppet classes to be installed on this node.

* For Collectd include te_performance::te_collectd
* For Grafana include te_performance::te_grafana
* For Graphite include te_performance::te_graphite
* For Metrics DWH include te_performance::te_metrics_dwh
* For Saiku include te_performance::te_saiku

        Example site.pp
            node '<Node name>' {   #e.g. node 'er3.vts.com'
            class { "te::te_graphite": } ->
            class { "te::te_grafana": }
            include te::te_saiku
            include te::te_metrics_dwh
            }
##Installation

###Puppet Master installation

Execute install_master.sh on gateway VM. This will download puppet-server and install all required dependencies.

    chmod +x install_master.sh && ./install_master.sh

**Important**
After installation all resources are copied to /etc/puppet folder. If editing is required refer to resources in /etc/puppet folders.

###Puppet Node installation

Next step is to install Puppet on other VMs. For each VM run install_node.sh script.

    scp root@<VM IP>:/root/install_node.sh .
    chmod +x install_node.sh && ./install_node.sh

##Maintenance

* Puppet master restart. Can be required if hiera.yaml was changed.

        sudo service puppetmaster restart

* If you have edited properties in #IP or #PORT section after install_master script run changes won't be applied to puppet server automatically. To apply changes run on gateway vm:

        sudo puppet apply /etc/puppet/manifests/gateway.pp

* This command will run puppet agent once. Can be useful to apply property changes to the node or if run was unsuccessful for some reason.
  Scenario can be the following: developer makes changes in configs on puppet master, then to apply changes on node he calls this command in target VM.

        sudo puppet agent -t --verbose --waitforcert 5 --onetime

*This command disables automatic puppet agent run (apply on each node). This is required if you wish to run agent manually after changing config on master puppet. Otherwise puppet agent can run automatically and you won't see output with results.

        sudo service puppet stop

##Verify Successful Install
Saiku will be accessible within \<gateway\>:\<saiku port\>/saiku-ui

Grafana will be accessible within \<gateway\>:\<grafana port\>
