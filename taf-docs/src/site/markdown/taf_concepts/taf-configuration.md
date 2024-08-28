<head>
   <title>TAF Configuration</title>
</head>

# TAF Configuration

The TAF Configuration is designed to provide access to the runtime configuration parameters in both the TAF framework and testware.

## Configuration Profiles
It is possible to pass one or multiple configuration profiles when test cases are being executed which would override a base set of properties.
The approach to define profiles, is to create directory called 'taf_profiles' in 'src/main/resources' in your testware and inside that folder, start creating
subdirectories according to profile names.

Then during the test the `-Dtaf.profiles` property should be passed. It is a comma-separated list of profile names loaded
in the exact same order e.g. `-Dtaf.profiles=profile-name1,profile-name2`.

Profiles are loaded using File System and Classpath configurations only.

**Note:** It is possible that the same profile name could be included in multiple testware packages, if this is the case the order is
chosen by classpath order. Care should be taken to ensure profile names are unique.

Examples of where profiles could be used:

* Different SUT installations and externalizing environment properties;

* Isolating RA, Team and other aggregated configurations;

* Creating configuration feature packs for reuse (example: selenium-grid)

## How configurations work in TAF

The following is the list of configuration sources by priority.
**Note:** Topmost sources override those below.

------------------------                                         | -------------------------
<span style="color:#ba3925;">Runtime Configuration</span>        | Runtime Configurations, which allows setting properties in runtime.
<span style="color:#ba3925;">System Configuration</span>         | Configurations found in System.getProperties().
<span style="color:#ba3925;">Environment Configuration</span>    | Configurations found in System.getEnv().
<span style="color:#ba3925;">User-defined Configuration</span>   | Configurations residing in <USER_HOME>/taf_properties directory.
<span style="color:#ba3925;">HTTP Configuration</span>           | Configurations retrieved by HTTP.
<span style="color:#ba3925;">DIT Configuration</span>            | Configurations retrieved from Deployment Inventory Tool (DIT).
<span style="color:#ba3925;">File System Configuration</span>    | Scans for taf_properties directory in the classpath.
<span style="color:#ba3925;">Classpath Configuration</span>      | Scans inside jar files in the current classpath.

All configuration sources can contain both `.json` or `.properties` configuration files.
**Note:** In the same configuration source, `.json` parameters have higher priority than `.properties` parameters.

### Runtime Configuration

TAF specific Map based in-memory API, which allows setting properties in runtime. To be used for custom property
data sources.

Any changes in the TAF configuration during runtime is stored in Runtime Configuration.

```java
    TafConfiguration configuration = TafConfigurationProvider.provide();
    String anyProperty = configuration.getString("any.property");
    assert anyProperty.equals({value from taf_properties configuration});
    configuration.setProperty("any.property","runtime.value");
    anyProperty = configuration.getString("any.property");
    assert anyProperty.equals("runtime.value");
```

### System Configuration

Configurations found in System.getProperties().

Example:

```
    mvn package -Dtestware.first.property=test  -Dtestware.second.property=test
```

```java
@TafProperty("testware.first.property")
private String first;

@TafProperty("testware.second.property")
private String second;
```

**Deprecated way:**

`
TafConfiguration configuration = TafConfigurationProvider.provide();
String first = configuration.getString("testware.first.property");
String second = configuration.getString("testware.second.property");
`

### Environment Configuration

Configurations found in <span style="color:#ba3925;">System.getEnv()</span>. You can assign/reassign this configuration parameters by settings system
environment variables.

### User-defined Configuration

Configurations residing in <USER_HOME>/taf_properties directory with recursive search.

### HTTP Configuration
Configurations retrieved by making a call to URI passed by taf.http_config.url property (both .json and .properties
formats), this can accept multiple URI's as a comma separated string

Example:

```
    mvn test -Dtaf.http_config.url=http://abc.ericsson.se/a/b/c
```

The Taf Configuration will make two requests for parameters:

* First: http://abc.ericsson.se/a/b/c?type=hosts to obtain the json hosts parameters

* Second: http://abc.ericsson.se/a/b/c?type=properties to obtain properties parameters

Example passing in 2 URI:

```
-Dtaf.http_config.url=http://abc.ericsson.se/a/b/c,http://efg.ericsson.se/e/f/g
```
The Taf Configuration will make 4 requests for parameters:

* First 2 requests as above followed by

* Third: http://efg.ericsson.se/e/f/g?type=hosts to obtain the json hosts parameters

* Fourth: http://efg.ericsson.se/e/f/g?type=properties to obtain properties parameters


### Configuration from DIT

Optional configuration source - DIT (Deployment Inventory Tool) external system.

#### Configuration Activation

Configuration retrieval is activated by providing deployment name via property `taf.config.dit.deployment.name`. Example:

```
mvn test -Dtaf.config.dit.deployment.name=ieatenmpd201-1
```

Internal nodes can also be specified to be accessible by using the parameter `taf.config.dit.deployment.internal.nodes` and passing 
in a single node or list of nodes separated by a comma. Example:
```
-Dtaf.config.dit.deployment.internal.nodes=fmx_1,amos_1
```
This allows all the details of the node to be retrieved.

#### TAF actions

Once DIT Configuration is activated TAF will send following requests (on first access to TAF Configuration, assuming deployment name is 'ieatenmpd201-1' and default URLs are used):

- http://atvts3395.athtem.eei.ericsson.se:3000/api/deployments?q=name=ieatenmpd201-1
- http://atvts3395.athtem.eei.ericsson.se:3000/api/documents/592c8cda82ee3e0020557633 (assuming first response returns sed_id = '592c8cda82ee3e0020557633')

#### Overriding DIT URLs

Parameter              | Default value                                    | Property key
---------------------- | ------------------------------------------------ | -------------------------
Base URL               | `http://atvts3395.athtem.eei.ericsson.se:3000/api` | `taf.config.dit.url`
Deployment URL pattern | `/deployments?q=name=%s`                           | `taf.config.dit.deployment.url.pattern`
Sed URL pattern        | `/documents/%s`                                    | `taf.config.dit.sed.url.pattern`

#### Configuration received

Configuration from DIT defines hosts (name, type, external IP and/or external IPv6).

Deployment private key from DIT will be available in TAF Configuration (`taf.config.dit.deployment.private.key` property).

#### Mapping host names

The default behaviour is the list of ips in a property are parsed into hosts with names which are indexed from 1.
 
For Example:
amos ips from DIT: "amos_external_ip_list": "131.160.206.101,131.160.206.102"

will be parsed into 2 host objects named amos_1 and amos_2

The names and numbering of certain hosts in DIT don't match what is expected by the tests. For that reason there is a mapping file in the repo.
  
For example:
haproxy hosts are indexed from 0, so a mapping is required:
haproxy=haproxy_0,haproxy_1

emp has only one instance so it has no numbering, so a mapping is required:
emp=emp

The format is:
<name_of_host_in_DIT>=<one_or_more_expected_hostnames>

A mapping can also be passed at runtime as a -D parameter.

##### Contributing a new host name mapping

Update the host-mapping.properties files in the ERICtaf_util repo and send it for review to the TAF team.

* Repo: https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.cifwk/ERICtaf_util
* File: ERICtaf_util/taf/taf-configuration/src/main/resources/host-mapping.properties

### File System Configuration

For IDE based or Maven based runs from the same module. Scans for `taf_properties` directory and all recursive entries
in the classpath. It is possible to override location of properties by passing `taf.properties.location` parameter.

### Classpath Configuration

Additional properties packed inside jar files in the current classpath. `taf_properties` and `subdirectories` are
scanned as well.

### TAF Properties

TAF properties are found in the `src/main/resources` directory in the taf_properties package. TAF provides many
different properties files and different properties within each file.

TAF has host.properties, datadriven.properties, monitors.properties among others.

These properties files contain vital information about the host environment and the data used in the tests
amongst other things.

### Properties Format

The format of a property is as follows:

```
    this.is.a.property=the property
```

The key uses . for spaces.

* There is no need to surround the value in inverted commas.

* It is possible to have a list of values in a property using comma to separate the values.

* To escape the special meaning of a comma place a backslash, \\, before it.

* **Note**: for properties in Runtime or Http configuration escape the comma with a double backslash, \\\\, before it.

Example

```
    #Simple property
    my-app.jndi=my-app-1.0/my-app-ejb-1.0/MyAppServiceBean!com.ericsson.cifwk.taf.MyAppService

    #Property which contains a list of values
    app.users.names=Tom, Joe, Andy
    app.users.passwords=1234, 2345, 3456

    #Property values containing commas
    command.create.enodeb.mecontext=cmedit create MeContext=node_id MeContextId=node_id\,
    neType=ENODEB\, generationCounter=2 -namespace=OSS_TOP -version=1.1.0
```

Properties can be interpolated in each other as follows:

```
    this.is.a.folder=folder
    this.is.a.subfolder=${this.is.a.folder}/subfolder
```

The value of property `this.is.a.subfolder` will be `folder/subfolder`

**NOTE:** Interpolation will only work when using type specific methods e.g. <span style="color:#ba3925;">getString(), getInt(), getBoolean()</span> and won't work with <span style="color:#ba3925;">getProperty()</span>

Since TAF **2.29.10** it's possible to reference TAF Host objects in the property files.

The naming convention is very simple: `${host:<host-name> port:<host-type> useSchema:<true | false>}`

Output is: `<schema>://<ip>:<port>`

Usage example: `login.url=${host:enmApache port:https useSchema:true}/login`

```json
[
  {
    "hostname": "enmApache",
    "ip": "10.43.251.3",
    "type": "httpd",
    "users": [
      {
        "username": "root",
        "password": "shroot",
        "type": "admin"
      }
    ],
    "ports": {
        "http": 8080,
        "https": 8443,
        "ssh" : 2022
    }
  }
]
```

The results will be the following:

`${host:enmApache port:https useSchema:true}/login` -> `https://10.43.251.3:8443/login`

`${host:enmApache port:https}/login` -> `10.43.251.3:8443/login`

`${host:enmApache}/login` -> `10.43.251.3/login`

**NOTE:** `useSchema` will be ignored for ports that is not http/https so:

`${host:enmApache port:ssh useSchema:true}/login` will be `10.43.251.3:2202/login` even if schema is set to `true`

Also default ports for http/https (80/443) will be omitted

### <a name="test_properties"></a>Test Properties

It is possible to pass additional options to the test case from TAF configurations via the <span style="color:#ba3925;">@TestOptions</span> annotation. This option accepts
a string that represents a value in the format <span style="color:#ba3925;">${expression}</span>.

For example, to set a testcase timeout (instead of defining a constant value) and to use this timeout in your test case you could define the following property in your properties file


```
defaultTestCaseTimeoutInMinutes=3
```

The above property could then be used in your test case like so:-

```java
@Test
@TestOptions(timeout = "${configuration['defaultTestCaseTimeoutInMinutes'] * 60 * 1000}")
public void mySimpleTestCase(){
    ...
    ...
    }
```

**NOTE:** Currently the `@TestOptions` annotation only supports the `timeout` property (as described above).

### Host Properties Json File

TAF has the functionality to consume host information from properties files in JSON format into Host objects.
The values within the host objects can then be used for whatever purposes needed.

The TAF module which automatically oversees the locating and loading of properties JSON files is the **Data Handler**.

The file must be located in one of the following places:

* In the `taf_properties` folder in the projects directory structure

* In the jar file in properties folder

* On the classpath

**Note:** The file extension must be `.properties.json` for properties in json format.

**Supported host types:**
For the full list of supported host types please see [Host Types] (https://taf.seli.wh.rnd.internal.ericsson.com/apidocs/Latest/com/ericsson/cifwk/taf/data/HostType.html)

#### Structure

<span style="color:#ba3925;">Host object structure</span>

All hosts are placed in an array. Each **host** should have **hostname**, **ip**, **type** and **ports** properties. **users** and **nodes**
properties are optional and have array of objects.

**hostname**, **ip**, **type** properties should be strings.

**ports** property is an object, which has key with name of protocol and value with port value in *Integer* format.

**Supported protocols:**
For a full list of supported protocols please see [Protocols] (https://taf.seli.wh.rnd.internal.ericsson.com/
.se/taflanding/apidocs/Latest/com/ericsson/cifwk/taf/data/Ports.html)

<span style="color:#ba3925;">User object structure</span>

**User** object has **username, password** and **type** properties. **username, password** are mandatory. Default value
 of **type** property is **OPER**.

**Supported user types**:
For a full list of supported user types please see [Users] (https://taf.seli.wh.rnd.internal.ericsson.com/
.se/taflanding/apidocs/Latest/com/ericsson/cifwk/taf/data/UserType.html)

<span style="color:#ba3925;">Node object structure</span>

The **node** object is same as host object. Only difference is that the **node** object doesn’t have child
property **nodes**.

#### Example

<span style="color:#ba3925;">Example hosts.properties.json file:</span>

```json
[
    {
        "hostname": "ms1",
        "ip": "0.0.0.1",
        "type": "ms",
        "users": [
            {"username": "root", "password": "shroot", "type": "admin"}
        ],
        "ports": {"ssh": 22}
    },
    {
        "hostname": "sc1",
        "ip": "0.0.0.2",
        "type": "sc1",
        "users": [
            {"username": "root", "password": "cobbler", "type": "admin"}
        ],
        "ports": {"ssh": 22},
        "nodes": [
            {
                "hostname": "jboss1",
                "ip": "0.0.0.2",
                "type": "jboss",
                "users": [
                    {"username": "root", "password": "shroot", "type": "admin"},
                    {"username": "guest", "password": "guestp", "type": "oper"}
                ],
                "ports": {
                    "http": 8080, "rmi": 4447, "jmx": 9999, "jboss_management": 9999
                }
            }
        ]
    },
    {
        "hostname": "CI-Netsim",
        "ip": "0.0.0.3",
        "type": "netsim",
        "users": [
            {"username": "netsim", "password": "netpass"}
        ],
        "ports": {"ssh": 2202}
    }
]
```

#### Example Usage

```java
@TafHost(type="netsim")
private Host netsimHost;
```

**Deprecated way:**

`
public Host getCINetsimHost() {
    return DataHandler.getHostByType(HostType.NETSIM);
}
`


### Host Properties File (Older format)

**WARNING** *This functionality is deprecated. Please, use new structure for host properties files: Host Properties Json File (above)*

#### Example

<span style="color:#ba3925;">Example hosts.properties file</span>

```
    host.ms1.ip=0.0.0.1
    host.ms1.user.root.pass=shroot
    host.ms1.user.root.type=admin
    host.ms1.port.ssh=22
    host.ms1.type=ms

    host.sc1.ip=0.0.0.2
    host.sc1.user.root.pass=cobbler
    host.sc1.user.root.type=admin
    host.sc1.type=sc1
    host.sc1.port.ssh=22

    host.sc1.node.jboss1.ip=0.0.0.2
    host.sc1.node.jboss1.type=jboss
    host.sc1.node.jboss1.user.root.pass=shroot
    host.sc1.node.jboss1.user.root.type=admin
    host.sc1.node.jboss1.user.guest.type=oper
    host.sc1.node.jboss1.user.guest.pass=guestp
    host.sc1.node.jboss1.port.http=8080
    host.sc1.node.jboss1.port.rmi=4447
    host.sc1.node.jboss1.port.jmx=9999
    host.sc1.node.jboss1.port.jboss_management=9999

    host.CI-Netsim.ip=0.0.0.3
    host.CI-Netsim.user.netsim.pass=netpass
    host.CI-Netsim.port.ssh=2202
    host.CI-Netsim.type=NETSIM
```

#### Structure

Each line in the properties file has the following structure:

```
    host.<hostname>.<hostParameter>=<value>
    host.<hostname>.port.<portType>=<value>
    host.<hostname>.node.<nodename>.<nodeParameter>=<value>
    host.<hostname>.user.<username>.pass=<password>
    host.<hostname>.user.<username>.type=<admin|oper>
```

* host, node, user and port are static prefixes

* &lt;hostname&gt;, &lt;nodename&gt;, &lt;username&gt; are replaced with the name of the host, node or user and should be unique in the context

* &lt;hostParameter&gt;, &lt;nodeParameter&gt; are replaced with whatever parameter you are setting

* &lt;value&gt; is the value of this parameter.
