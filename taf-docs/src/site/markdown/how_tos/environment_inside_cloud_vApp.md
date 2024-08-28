<head>
   <title>TAF connecting to environment inside cloud vApp</title>
</head>

# TAF connecting to environment inside cloud vApp

**Problem:**

Possibility to interact with SUT in situation when nodes are inside cloud vApp. Node inside vApp are shared on private IP addresses
visible only inside the vApp and access to the nodes is via gateway available under one public IP address.

**Port forwarding:**

Gateway usually provides set of rules allowing connection to particular services inside vApp using port forwarding. To list these rules, use following command:
```
iptables -t nat -L PREROUTING -n
```

Last column shows the IP address and port where connection incoming to port marked as dpt. The incoming port should be listed in host
properties file as the destination port.

## Example of SSH port forwarding

Port forwarding to SSH port of OSS-RC master host is visible as following:

```
DNAT       tcp  --  0.0.0.0/0            0.0.0.0/0           tcp dpt:2205 to:192.168.0.5:22
```

This means that connection to gateway on port 2205 will be forwarded to MS IP address (192.168.0.5) and default SSH port (22).

This setting can be reflected in host properties by following lines:

```
host.ms.ip=atvtsXXX.athtem.eei.ericsson.se
host.ms.port.ssh=2205
```

where atvtsXXX.athtem.eei.ericsson.se is actual public address of gateway.

**Setting port forwarding**

It may be a case when the default set of port forwarding rules does not contain a rule for a particular port. For example, a required rule is to allow connection to be forwarded to port 8080 on SC1 node of the environment. To create appriopriate rule, run command:

```
iptables -t nat -A PREROUTING -p tcp --dport 18080 -j DNAT --to-destination 192.168.0.43:8080
```

where 192.168.0.43 is IP address of SC1 inside the cloud vApp and 18080 is port we want to be redirected.

To reflect following rule in host properties:

```
host.sc1.ip=atvtsXXX.athtem.eei.ericsson.se
host.sc1.port.http=18080
```

**Using tunneling**

Redirected SSH ports can be used for SSH tunneling. Example of using 8080 port to SC1, where this port is not exposed can be done as following:

```
host.sc1.ip=atvtsXXX.athtem.eei.ericsson.se
host.sc1.port.ssh=2243
host.sc1.node.jb1.port.http=8080
host.sc1.node.jb1.tunnel=1
```

For more information about port forwarding, please refer to documentation http://www.netfilter.org/documentation/HOWTO/NAT-HOWTO-6.html#ss6.2
