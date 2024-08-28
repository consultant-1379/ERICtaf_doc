<head>
    <title>Grafana and DDC</title>
</head>

#Grafana and DDC

##DDC Documentation

To get a background on DDC and the functionality and metrics it supplies please see [DDC documentation](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/Hawkeye/DDC+DDP)

##Set-up TAF DDC Handler on SUT

1. Copy the following script to the **MS**

        \#!/usr/bin/expect
        cd /tmp
        wget http://cifwk-oss.lmera.ericsson.se:8081/nexus/service/local/repositories/snapshots/content/com/ericsson/cifwk/taf-ddc-handler-rpm/2.13.6-SNAPSHOT/taf-ddc-handler-rpm-2.13.6-20150922.145727-1.rpm
        litp import /tmp/taf-ddc-handler-rpm-*.rpm /var/www/html/ENM_common/
        litp create -p /software/items/taf-ddc-handler -t package -o name=taf-ddc-handler-rpm
        litp inherit -p /ms/items/taf-ddc-handler -s /software/items/taf-ddc-handler
        litp inherit -p /deployments/enm/clusters/svc_cluster/nodes/svc-1/items/taf-ddc-handler -s /software/items/taf-ddc-handler
        litp inherit -p /deployments/enm/clusters/svc_cluster/nodes/svc-2/items/taf-ddc-handler -s /software/items/taf-ddc-handler
        litp inherit -p /deployments/enm/clusters/db_cluster/nodes/db-1/items/taf-ddc-handler -s /software/items/taf-ddc-handler
        litp inherit -p /deployments/enm/clusters/db_cluster/nodes/db-2/items/taf-ddc-handler -s /software/items/taf-ddc-handler
        list=`litp show -p /software/services/`
        vmlist=`echo $list | sed -e 's/.*children: //'`
        newvmlist=`echo $vmlist | sed -e 's./..g'`
        for vm in $newvmlist; do litp create -t vm-package -p /software/services/$vm/vm_packages/taf-ddc-handler -o name=taf-ddc-handler-rpm; done
        litp create_plan
        litp run_plan

    The script can be downloaded [here](../installddc.sh)

2. Run the script

        ./installddc.sh

    **Note** The script may give errors such as the following, these can be ignored

        /software/services/versant_service/vm_packages/taf-ddc-handler
        InvalidLocationError    Path not found

3. LITP plan can take up to 40 minutes to complete. To check the plan progress you can use

        litp show plan -a

4. DDC Handler is now on the classpath of DDC

5. Ensure Masquerading is setup on the MS and node you require performance data on, to check this run the following on the MS
    Check Masquerading

        \[root@ieatlms4403-1 ~\]\# iptables -t nat -L
        Chain PREROUTING (policy ACCEPT)
        target     prot opt source               destination
        Chain POSTROUTING (policy ACCEPT)
        target     prot opt source               destination
        MASQUERADE  all  --  anywhere             anywhere
        Chain OUTPUT (policy ACCEPT)
        target     prot opt source               destination
        \[root@ieatlms4403-1 ~\]\#

    If Masquerading is not configured, run the following on the MS

        [root@ieatlms4403-1 tmp]#  /sbin/iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
        [root@ieatlms4403-1 tmp]# /sbin/iptables -A FORWARD -i eth0 -o eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT
        [root@ieatlms4403-1 tmp]# /sbin/iptables -A FORWARD -i eth1 -o eth0 -j ACCEPT

    Check to ensure GW is setup on vm you require performance data from

           root@svc-1-cmserv ~]# netstat -rn
           Kernel IP routing table
           Destination     Gateway         Genmask         Flags   MSS Window  irtt Iface
           10.140.14.0     0.0.0.0         255.255.255.0   U         0 0          0 eth0
           141.137.210.0   0.0.0.0         255.255.255.0   U         0 0          0 eth0
           10.250.244.0    0.0.0.0         255.255.252.0   U         0 0          0 eth2
           10.247.244.0    0.0.0.0         255.255.252.0   U         0 0          0 eth1
           224.0.0.0       0.0.0.0         240.0.0.0       U         0 0          0 eth2
           0.0.0.0         10.140.14.47    0.0.0.0         UG        0 0          0 eth0

    If no GW present on the node run the following, the IP used here is the IP off eth1 on MS:

            route add default gw 10.140.14.47 eth0

6. Log into vm you require performance data on and start ddc by running "service ddc start"

7. To verify that it is running succesfully you can log into rabbitmq and ensure connection has opened. You should see the IP of the MS

        [root@atclvm793 ~]# tail -f /sdb/rabbitmq/rabbitmq_server-3.0.4/var/log/rabbitmq/rabbit\@atclvm793.log
        =INFO REPORT==== 21-Jul-2015::14:14:19 ===
        accepting AMQP connection <0.3056.414> (141.137.210.216:44998 -> 10.59.140.185:5672)

8. To visualize Metrics sent by DDC please see [Grafana Usage](grafana_use.html)

**Note** The taf-ddc-handler is configured to use the following: host.rabbitmq.ip=atclvm793.athtem.eei.ericsson.se and amqp.exchange=taf.samples by default
It re-configure the message bus host please update the following file on the host you are monitoring after installation

        /opt/ericsson/ERICddc/lib/graphite.properties
