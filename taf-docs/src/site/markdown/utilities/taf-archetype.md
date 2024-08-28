<head>
   <title>Utilities</title>
</head>

# Utilities

## TAF Archetype

All TAF projects are Maven based and must have a dependency on TAF. TAF provides a Maven archetype, which is a template TAF project.
The archetype includes all the dependencies and plugins needed to write TAF test cases.

### Archetype structure

TAF **strongly** recommends the following structure for TAF testware:

* Test cases are kept in proper package under test package. This part of a testcase is not shared amongst teams and is
  specific to each case.

* Getters and operators kept in properly named packages in the operators module of your git repo. These packages can be
  shared with the rest of the community via maven dependency injection of the operator module, but their namespace needs
  to be preserved in relation to their business logic.

* Non-business logic kept in **com.ericsson.cifwk.taf packages**. This logic will be incorporated inside TAF and delivered in
  TAF jar. These packages are used to extend the TAF framework itself.

The TAF directory structure consists of a top level parent project that has three sub modules; the test-pom module, the ERICTAF package
module and operators package module.

The ERICTAF module contains your testware artifact. It is this module that should contain all plugins and dependencies needed for the
building of your artifact. The second module, test-pom, is concerned with the execution of your testware. It is here where runtime
dependencies or plugins should be included. The third module, operators, contains your business logic code used by your test which
could be shared with other users.

### Generating the archetype

:------- | :----------
**NOTE** | In order to get the TAF archetype, your maven settings.xml file (in your .m2 folder) needs to reference the PDU OSS Cifwk Nexus. Instructions can be found [here](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/CIE/settings.xml).

To generate a TAF project using TAF archetype, change directory to the folder in which you want the TAF project to be created and invoke
the command based on the following structure:

```java
    mvn -U com.ericsson.cifwk.taf:taf-maven-plugin:archetype  \
    -DgroupId="<enter your group Id here>" \
    -DartifactId=<enter your artifactID here> \
    -Dversion=<enter your version here> \
    -DparentGroupId="<enter the groupId of the parent pom you will to use>" \
    -DparentArtifactId="<enter the artifactId of the parent you want to use>" \
    -DparentVersion=<enter the version of the parent you want to use>   \
    -DcxpNumber=<enter the cxp number here>
```java

**Note**: Here is an example:

```java
    mvn -U com.ericsson.cifwk.taf:taf-maven-plugin:archetype  \
    -DgroupId="com.ericsson.group" \
    -DartifactId=myNewArtifact \
    -Dversion=1.0.1-SNAPSHOT \
    -DparentGroupId="com.ericsson" \
    -DparentArtifactId="master" \
    -DparentVersion=1.2 \
    -DcxpNumber=CXP88888888
```java

:------- | :----------
**NOTE** | All the above -D options are mandatory

This will list all the archetypes in Nexus so you need to select the TAF option:

```
Choose archetype:
1: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.oss.itpf.sdk.archetypes:service-framework-war-archetype (-)
2: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.archetypes:service-framework-ear-archetype (-)
3: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.oss.itpf.sdk.archetypes:service-framework-ear-archetype (-)
4: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:mediation-set-archetype (-)
5: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:mediation-contribution-archetype (-)
6: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:mediation-component-archetype (-)
7: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:mediation-model-archetype (-)
8: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.litp:litp-extension-archetype (-)
9: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.cds:cds-archetype (-)
10: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:event-handler-archetype (-)
11: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.archetypes:sonpm-componnent-archetype (-)
12: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.archetypes:service-framework-war-archetype (-)
13: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:helloworld-handler-archetype (-)
14: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.nms.mediation:helloworld-flow-archetype (-)
15: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.oss:ci_execution_sample_archetype (-)
16: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.cifwk:taf-archetype (-)
Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): :
```

You are looking for the option

```
https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/releases/archetype-catalog.xml -> com.ericsson.cifwk:taf-archetype (-)
```

:------- | :----------
**NOTE** | The above list may vary depending on the number of archetypes in Nexus at the moment in time when you create the archetype.

Next you will need to choose the version of the archetype you want

```
Choose com.ericsson.cifwk:taf-archetype version:
1: 2.2.1
2: 3.0.1
3: 3.0.2
4: 3.0.3
Choose a number: 4:
```

TAF recommend you choose the latest version.

:------- | :----------
**NOTE** | Previous versions are retained for reference only.

Next you will need to specify which TAF SDK you will use in your project and confirm your options.

```
[INFO] Using property: taf_ = sdk:[ all | assure | ossrc | tor ]:
Define value for property 'taf_sdk': : all                       //Choose your TAF SDK
Confirm properties configuration:
groupId: com.ericsson.testing
artifactId: myartifactId
version: 1.0.1-SNAPSHOT
package: com.ericsson.testing
taf_: sdk:[ all | assure | ossrc | tor ]
taf_sdk: all
 Y: : Y										                    //Review and agree your options
```

This will create the following file structure:

```
.
├── ERICTAFmyNewArtifact_CXP88888888
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── ericsson
│           │           └── group
│           └── resources
│               ├── data
│               ├── log4j.properties
│               ├── suites
│               └── taf_properties
├── ERICTAFmyNewArtifact_operators
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── ericsson
│       │   │           └── group
│       │   └── resources
│       │       └── log4j.properties
│       └── test
│           ├── java
│           │   └── com
│           │       └── ericsson
│           │           └── group
│           └── resources
├-── test-pom-myNewArtifact
│    └── pom.xml
└── pom.xml
```

:------- | :----------
**NOTE** | The parent groupID, artifactId and version above will depend on what RA the TAF project will belong to, the options are:-

1. **<span style="color:#ba3925;">ENM</span>** https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.nms/integration

2. **<span style="color:#ba3925;">OSS-RC</span>** https://gerrit.ericsson.se/#/admin/projects/OSS/com.ericsson.oss/integration

The project is now ready for use and can be compiled using the following command:

```
mvn clean install
```

Here is how the full flow would look like:- [creating an archetype](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/archetype_terminal.html)

## Using the archetype

When writing your tests, all the test case classes should be in the ERICTAFmyNewArtifact_CXP88888888 module, any business logic code specific to your
team should also be in the ERICTAFmyNewArtifact_operators module. If you end up with an operator which you think would be useful to other teams you
should add it into the specific shared test libraries. This will facilitate easy sharing of your operator without other teams inadvertently
running your tests.

## Updating to the latest archetype

If desired, it is possible (but not mandatory) to update to the latest archetype, there are two options

1. Use the [taf maven plugin](taf-maven-plugin.html) to create an archetype and copy across your current code to the relevant folder **(this is the approach recommended by TAF)**

2. Manually restructure your current repo to reflect the structure shown above

<span style="color:#ba3925;">Manual Option</span>

If you are experienced and confident with maven you can manually migrate your project.

1. Create a new module, ERICTAF&lt;artifactId&gt;_operators, on the same level as the ERICTAFmyNewArtifact_CXP888888 and test-pom-myNewArtifact modules.

2. Move your operator java classes to the new module.

3. Move any necessary files from resources to the new module.

4. Move any necessary maven configuration to the new module.

5. Put a dependency in the ERICTAFmyNewArtifact_CXP888888 module on the new module.

6. Update the modules section in the top level pom to include the new module (ERICTAF&lt;artifactId&gt;_operators)

## Updating the version of TAF

The version of TAF is now specified in a single location in the top level pom

```
.....
 <properties>
    <taf_version>2.33.1</taf_version>
 </properties>
....
```

:------- | :-----------
**NOTE** | Updating this version updates the version of TAF for the whole project

## Using TAF FindBugs Rules

By default generated pom.xml contains FindBugs plugin, which is preconfigured to test your testware against [TAF testware design rules](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/pages/viewpage.action?spaceKey=TAF&title=TAF+Test+Ware+Design+Rules).

To run TAF FindBugs check locally please run (in project root folder):
`mvn clean install -Pfindbugs -DskipTests`

To see the report please run (in project root folder):
`mvn -Pfindbugs findbugs:gui`







