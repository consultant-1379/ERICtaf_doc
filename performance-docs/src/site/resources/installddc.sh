
#!/usr/bin/expect

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


