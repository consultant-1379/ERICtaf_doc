<head>
   <title>Data Handler</title>
</head>

# TAF Data Handler

TAF Data Handler is a tool for data management. It retrieves all the data from 
[various locations](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html) 
and makes it available for test code.

Additionally, TAF Data Handler can be used to search and create objects that represent hosts. 

# Properties

## Injection

Properties from [various locations](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html) could be injected right into your test (or operator) class:

```java
@TafProperty("timeoutMillis")
long timeoutMillis;
```

If property is not found in TAF configuration - runtime exception will be thrown during test initialization (error message will contain exact class and field names failed to be injected).

## Default value

Absence of configuration might be OK for your test (e.g. you can handle it yourself or you have default value). In that case to avoid runtime exception:

```java
TafProperty(value = "timeoutMillis", defaultValue = "2000")
private long timeoutMillis;
```

## Late binding

To look up property value at runtime (instead of test initialization time) please use late binding:

```java
TafProperty(value = "timeoutMillis", defaultValue = "2000")
private Provider<Long> runtimeProperty;
```

This technique:
 - allows accessing runtime properties (set during test execution)
 - avoids failing all tests because of single property misconfigured - in this case only tests using the property will fail

Providers supported:
 - `javax.inject.Provider` (JSR330)
 - `com.google.inject.Provider` (Guice)

## Supported types

TAF Property injection supports *String*, *Java primitive types* and their *wrapper classes* as well as several additional types:

```java
@TafProperty(INTEGER_KEY)
private BigDecimal bigDecimal;

@TafProperty(INTEGER_KEY)
private Provider<BigInteger> bigInteger;

@TafProperty(CLASS_KEY)
private Class clazz;

@TafProperty(FILE_KEY)
private Provider<File> file;

@TafProperty(URL_KEY)
private URL url;
```

## Programmatic access

<div class="note"></div>
**NOTE**: It is **recommended** to **use property injection** (like described above).

However sometimes injection is not enough (e.g. getting property in static code or using [Custom predicates](#search)). In that case programmatic access to TAF properties is required.
For convenience, TAF Data Handler has `getAttribute` and `setAttribute` methods:

```java
TafDataHandler.setAttribute("name", "Value");
String attribute = TafDataHandler.getAttribute("name");
String attribute2 = TafDataHandler.getAttribute("name2", "defaultValue");
```

Attributes set at runtime using the `TafDataHandler.setAttribute` override attributes from files and environment variables.

For advanced data manipulation it is recommended to use [Taf Configuration](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html)
That is available using `TafDataHandler.getConfiguration()` method.

# Hosts

## Injection

Host object (from TAF Configuration) could be injected right into your test (or operator) class:

```java
@TafHost(hostname = "ms1")
private Host ms1Host;
```

If **host not found** - runtime exception (error message will contain all *available hosts*).<br/>
If **more then one hosts found** matching *search criteria* - runtime exception (error message will contain *found hosts*).

## Search criteria

It is possible to search Hosts by different criteria and their combinations:

```java
@TafHost(hostname = "ms1")
private Host ms1Host;

@TafHost(group = "msfm")
private Host hostByGroup;

@TafHost(type = "netsim")
private Host hostByType;

@TafHost(hostname = "ms1", group = "msfm", type = "netsim")
private Host hostByAllAvailableCriterias;
```

## Multiple hosts

Absence of Host might be OK for your test (e.g. you can handle that case yourself). Also there could be multiple hosts found matching search criteria provided in annotation. To handle such cases please use collection type fields:

```java
@TafHost(type = "netsim")
private List<Host> hostsByType;

@TafHost
private Set<Host> allHosts;

@TafHost(group = "non-existing-group")
private Collection<Host> emptyCollection;
```

## Programmatic access

<div class="note"></div>
**NOTE**: It is **recommended** to use TAF **injection mechanism** to load hosts.

TAF Data Handler may be used in places where Dependency Injection is not available (for example static methods), and for specific cases, for example for using [Custom predicates](#search).
Host could be filtered using the following API:

```java
List<EnmNetworkHost> hostsByCriteria = TafDataHandler
        // Type of Hosts
        .findEnmHost() // or .findHost()
        // Search criteria
        .withGroup() // or .withHostname()
        // Result format
        .getAll(); // or .get() or .getFirst()
```


### Types Of Hosts

Currently TAF Data Handler can create two types of hosts:

* `TafDataHandler.findHost()` which returns `com.ericsson.cifwk.taf.data.Host` in [legacy](#migration) format.
* `TafDataHandler.findEnmHost()` **(recommended)** which returns `com.ericsson.cifwk.taf.data.network.EnmNetworkHost`

<a id="search"></a>
### Fluent search

It is possible to filter hosts by one or more search criteria:

```java
List<EnmNetworkHost> hostsByCriteria = TafDataHandler
        .findEnmHost()
        .withGroup("group")
        .withHostname("hostname")
        .withType("type")
        //...
        .getAll();
```

For specific cases it is possible to search hosts using custom predicate:

```java
EnmNetworkHost thehost = TafDataHandler
    .findEnmHost()
    .withPredicate(
        new Predicate<HostFilter>() {
            @Override
            public boolean apply(HostFilter input) {
                return "thehost".equals(input.getHostName()); //more custom conditions
            }
        }
    ).get();
```

Predicates work with generic bean `com.ericsson.cifwk.taf.configuration.HostFilter` which does not represent any specific type of Host, and have majority of fields available in [configuration](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/taf_concepts/taf-configuration.html).

### Nullable

If `nullable()` not set, `TafDataHandler` will throw exception if no host found.

If `nullable()` is set, `TafDataHandler` will return `null` if no host found:

```java
Host thehost = TafDataHandler.findHost()
        .withHostname("notexisting")
        .nullable()
        .get();

assertThat(thehost).isNull();
```

### Result format

It is possible get a single host or list of all hosts that match defined criteria.

* `.get()` Gets one host that matches given criteria. Will throw exception if more than one host found, use `.getFirst()` for different requirements. If [#nullable()](#nullable) is not set, exception will be thrown if no host found.
* `.getFirst()` Gets first host that matches given criteria, even if more than one host found. If [#nullable()](#nullable) is not set, exception will be thrown if no host found.
* `.getAll()` Gets all hosts that match given criteria

<a id="migration"></a>
# Migration from Legacy Data Handler

It is recommended to use subclasses of `com.ericsson.cifwk.taf.data.network.EnmNetworkHost` instead of `com.ericsson.cifwk.taf.data.Host` for the following reasons:

1. More robust implementation
2. Fewer dependencies
3. No overhead of legacy functionality such as tunneling

`TafDataHandler` can work as drop-in replacement for `DataHandler`.

Replace:

`com.ericsson.cifwk.taf.data.DataHandler`

With:

`com.ericsson.cifwk.taf.configuration.TafDataHandler`

`TafDataHandler` supports specific legacy methods, but they are deprecated:

```java
List<Host> oldApi = TafDataHandler.getAllHostsByType(HostType.NETSIM);
```

Suggestion is to replace specific calls with fluent search:

```java
List<Host> newApi = TafDataHandler.findHost().withType(HostType.NETSIM).getAll();
```