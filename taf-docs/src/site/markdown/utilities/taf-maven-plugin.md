<head>
   <title>TAF Maven Plugin</title>
</head>

# TAF Maven Plugin

## What is Maven Plugin?

"Maven" is really just a core framework for a collection of Maven Plugins. In other words, plugins are where much of the real action is performed.
Plugins are used to: create jar files, create war files, compile code, unit test code, create project documentation, and so forth. Almost any
action that you can think of performing on a project is implemented as a Maven plugin.

## What is the TAF Maven Plugin?

The "TAF Maven Plugin" is used to minimize the size of the Maven project file and add new behaviour
to your project’s build. The TAF Maven Plugin is used during the <span style="color:#ba3925;">test</span> phase of the build lifecycle to execute the TAF
tests of an application and on the <span style="color:#ba3925;">clean</span> phase to clean TAF test execution artifacts(reports, temporary files, etc.). During the
compile phase the <span style="color:#ba3925;">analysis</span> goal can be run to validate that code compiles based on certain rules.
A new goal has been added at the <span style="color:#ba3925;">generate sources</span> phase which generates a project from the TAF archetype and configures the project in question.

## How to get Started

If you created your testware project using the "TAF Maven Archetype" then "TAF Maven Plugin" is already in your pom.xml. Otherwise, add "TAF Maven Plugin"
into section build section, and configure the execution of the test goal in executions section, as in this example:

If you created your testware project using the "TAF Maven Archetype", then "TAF Maven Plugin" is already in your pom.xml.
Otherwise, add "TAF Maven Plugin" into the **build** section, and configure the execution of the <span style="color:#ba3925;">test</span> goal
in the <span style="color:#ba3925;">executions</span> section, as in this example:

```
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>com.ericsson.cifwk.taf</groupId>
                <artifactId>taf-maven-plugin</artifactId>
                <version>{taf.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ...
</project>
```

## Current Functionality

The TAF plugin has four goals:

* <span style="color:#ba3925;">analysis</span> - verifies that TAF Test files comply with rules

* <span style="color:#ba3925;">archetype</span> - runs the generate archetype goal and configures the project.

* <span style="color:#ba3925;">clean</span> - cleans the TAF test run artifacts

* <span style="color:#ba3925;">test</span> - runs the TAF tests of an application

**<span style="color:#ba3925;">Analysis</span>**

**Description:** Produces actions if code does not comply with certain rules. Rules can be on a project, class or method level.

This **goal** checks classes, methods or projects against rules to see if they comply with the standards set out by the rules. If any of the rules are violated,
an action is performed, depending on the priority of the rule.
There are 4 Priority Levels:

* INFO - Logs information about what was violated.

* WARNING - Logs a warning.

* FAILURE - Logs an error.

* CRITICAL - Throws a MojoExecutionException stating the rule that has been violated.

This goal has one configuration parameter which can be configured into the plugin configuration section.

Name           | Type      | Description
:---------     | :---------| :---------------------
failThreshold  | Priority  |  If a rule has a priority greater than the threshold then it will use the action associated with failThreshold priority rather than the action associated with its own priority. **Default value is:** INFO. **User property is:** failThreshold.

**Note:** failThreshold is the maximum priority that you want to report at. For example, if failThreshold level is set to **WARNING**, all INFO level rules will report
          at INFO level but all priorities at level WARNING and above will report at WARNING level..

**How to use:**

Can be run from the command line: **mvn com.ericsson.cifwk.taf:taf-maven-plugin:analysis**

Can be added to test module pom.xml file

```
<project>
    ...
        <build>
            <plugins>
                <plugin>
                    <groupId>com.ericsson.cifwk.taf</groupId>
                    <artifactId>taf-maven-plugin</artifactId>
                    <executions>
                        <execution>
                            <goals>
                                <goal>analysis</goal>
                            </goals>
                            <configuration>
                                <failThreshold>WARNING</failThreshold>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    ...
</project>
```

**Rules:**

Name                          | Priority  | Rule Type | Description
:---------------------        | :-------  | :-------  | :-------------------------
ValidateTestHasCorrectSetUp   | CRITICAL  | Method    | Validate that test case has a test case id
ValidateJavaDoc               | WARNING   | Method    | Validate that test case has JavaDoc
ValidateNoPrintStatements     | WARNING   | Class     | Validate that class does not have print statements
ValidateNoStackTrace          | WARNING   | Class     | Validate that class does not call printStackTrace
ValidateTestClass             | CRITICAL  | Class     | Validate that test class extends TestCaseHelper
ValidateTestClassHasNoGetters | FAILURE   | Class     | Validate that test class does not contain getters
ValidateTestwareModuleName    | CRITICAL  | Project   | Validate that default testware name ERICTAFmodulename_CXPxxxxxxxx is not being used

**<span style="color:#ba3925;">archetype</span>**

**Description:** Generate a project from the TAF archetype.

This goal generates a project from the TAF archetype and configures that project with user specific info. For more information please see section: Taf Archetype

**<span style="color:#ba3925;">clean</span>**

**Description:** Removes TAF test run artifacts that were not created in the target folder.

The TAF maven plugin removes <span style="color:#ba3925;">/test-output</span> and <span style="color:#ba3925;">/tmp</span> folders, created by TAF runner. Also, the plugin deletes<span style="color:#ba3925;">curl.exe, libeay32.dll</span> and <span style="color:#ba3925;">libssl32.dll</span> files, created by TAF runner.

This goal has no configuration parameters. **How to use:** Add the plugin into the build section of the maven build lifecycle. Add clean goal into the plugin execution section.

**<span style="color:#ba3925;">test</span>**

**Description:** Prepares project to perform TAF test executions and execute TAF tests

This goal has several steps. Each step can be configured in plugin configuration section.

1. unpack your testware suites into /target/suites folder

2. copy TAF into /target/lib folder

3. copy your testware into /target/lib folder

4. unpack Selenium drivers into /target/selenium-drivers folder

5. execute TAF tests for your testware

6. Each step may be configured into the plugin configuration section.

Attributes:

Name                | Type        | Description
:----------------   | :-------    | :---------------------------------------
skipTests           | boolean     | Set this to <span style="color:#ba3925;">true</span> to skip running tests, but still prepare them. **Default value is:** <span style="color:#ba3925;">false.</span> **User property is:** <span style="color:#ba3925;">skipTests.</span>
mainClass           | String      | TAF runner main class. **Default value is:** <span style="color:#ba3925;">com.ericsson.cifwk.taf.Taf</span>
commandlineArgs     | String      | TAF execution command line arguments.
projectName         | String      | TAF execution projectName property. **Default value is:** ${project.name}. **User property is:** <span style="color:#ba3925;">projectName</span>.
name                | String      | TAF execution name property. **Default value is:** <span style="color:#ba3925;">TAFTEST</span>. **User property is:** <span style="color:#ba3925;">name</span>.
suites              | String      | Comma separated list of suites xml files. **Default value is:** All suites xml in directory defined by <span style="color:#ba3925;">dir</span> attribute. **User property is:** <span style="color:#ba3925;">suites</span>.
dir                 | String      | TAF suites directory. **Default value is:** value, define in suitesXml.outputDirectory attribute.
groups              | String      | Comma separated list of test groups. **User property is:** <span style="color:#ba3925;">groups</span>.
properties          | Properties  | A list of system properties to be passed. **Note:** as the execution is not forked, some system properties required by the JVM cannot be passed here. Use <span style="color:#ba3925;">MAVEN_OPTS</span> instead. See the user guide for more information. **Note:** This property override previously defined properties in command line or maven pom.xml.  **Note:** suites and groups property defined in command line or in the plugin configuration override the same properties defined by this attribute.
**suitesXml**       | Map         | "TAF Maven Plugin" use the [Maven dependency plugin](http://maven.apache.org/plugins/maven-dependency-plugin/) for suites unpacking. For for suites unpack "TAF maven plugin" execute the goal [dependency:unpack-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/unpack-dependencies-mojo.html). All attributes from suitesXml attributes maps to the relevant attributes of a goal [dependency:unpack-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/unpack-dependencies-mojo.html).
includeGroupIds     | String      | Comma separated list of GroupIds to include. **Default value is:** ${project.groupId}.
includeArtifactIds  | String      | Comma separated list of ArtifactIds to include.
excludeGroupIds     | String      | Comma separated list of GroupIds to exclude.
excludeArtifactIds  | String      | Comma separated list of ArtifactIds to exclude.
outputDirectory     | String      | Output location. **Default value is:** ${project.build.directory}/suites.
**copyDependencies**| Map         | "TAF Maven Plugin" use the [Maven dependency plugin](http://maven.apache.org/plugins/maven-dependency-plugin/) for artifacts collecting. For for suites unpack "TAF maven plugin" execute the goal [dependency:copy-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/copy-dependencies-mojo.html). All attributes from suitesXml attributes maps to the relevant attributes of a goal [dependency:copy-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/copy-dependencies-mojo.html).
includeGroupIds     | String      | Comma separated list of GroupIds to include. **Default value is:** ${project.groupId}.
includeArtifactIds  | String      | Comma separated list of ArtifactIds to include.
excludeGroupIds     | String      | Comma separated list of GroupIds to exclude.
excludeArtifactIds  | String      | Comma separated list of ArtifactIds to include.
outputDirectory     | String      | Output location. **Default value is:** <span style="color:#ba3925;">${project.build.directory}/lib</span>.
**selenium**        | Map         | "TAF Maven Plugin" use the [Maven dependency plugin](http://maven.apache.org/plugins/maven-dependency-plugin/) for selenium drivers collecting. For suites unpack "TAF maven plugin". Execute the goal [dependency:copy-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/copy-dependencies-mojo.html). All attributes from suitesXml attributes map to the relevant attributes of a goal [dependency:copy-dependencies](http://maven.apache.org/plugins/maven-dependency-plugin/copy-dependencies-mojo.html).
includeGroupIds     | String      | Comma separated list of GroupIds to include. **Default value is:** <span style="color:#ba3925;">com.ericsson.cifwk.selenium.drivers</span>.
includeArtifactIds  | String      | Comma separated list of ArtifactIds to include.
outputDirectory     | String      | Output location. **Default value is:** <span style="color:#ba3925;">${project.build.directory}/selenium-drivers</span>.

## FAQ & HOWTOs

<span style="color:#ba3925;">How to config testware artifacts</span>

1. Add your testware artifacts as dependencies into test-pom/pom.xml

2. Define test-pom/pom.xml groupId, the same as your testware groupIds.

3. "TAF Maven Plugin" use Maven dependency plugin for suites unpacking

**<span style="color:#ba3925;">test-pom/pom.xml</span>**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.ericsson.test</groupId>
    <artifactId>test-pom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
     ...
    <properties>
        <taf_version>2.1.1</taf_version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>com.ericsson.test.testware</groupId>
            <artifactId>ERICabc_CXPxxxxxxxx</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.ericsson.cifwk.taf</groupId>
                <artifactId>taf-maven-plugin</artifactId>
                <version>${taf_version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

<span style="color:#ba3925;">How to configure testware artifacts for specific testware execution</span>

1. Add your testware artifacts as dependencies into test-pom/pom.xml

2. Define test-pom/pom.xml groupId, the same as your testware groupIds.

3. Define parameter includeArtifactIds in suiteXml attribute as your testware artifactId

**Example:**

Only ERICabc_CXP0000002 and ERICabc_CXP0000002 testware artifact suites executed by TAF maven plugin

**<span style="color:#ba3925;">test-pom/pom.xml</span>**

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.ericsson.test</groupId>
    <artifactId>test-pom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
     ...
    <properties>
        <taf_version>2.0.9-SNAPSHOT</taf_version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>com.ericsson.test.testware</groupId>
            <artifactId>ERICabc_CXP0000001</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ericsson.test.testware</groupId>
            <artifactId>ERICabc_CXP0000002</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ericsson.test.testware</groupId>
            <artifactId>ERICabc_CXP0000003</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.ericsson.cifwk.taf</groupId>
                <artifactId>taf-maven-plugin</artifactId>
                <version>${taf_version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <suitesXml>
                        <includeArtifactIds>ERICabc_CXP0000002,ERICabc_CXP0000003
                        </includeArtifactIds>
                    </suitesXml>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

<span style="color:#ba3925;">How to skip TAF test execution</span>

Add -DskipTafTests parameter into maven command

**Example:**

```
mvn package -DskipTafTests
```

**Note:** -DskipTests parameter will skip both Unit/Integration Tests & TAF tests.

<span style="color:#ba3925;">How to execute concrete suite xml’s</span>

By default, the TAF Maven plugin execute all suite xml files found in testware. It is possible to define concrete suite names using the suites configuration parameter.

**<span style="color:#ba3925;">test-pom/pom.xml</span>**

```
        <plugin>
            <groupId>com.ericsson.cifwk.taf</groupId>
            <artifactId>taf-maven-plugin</artifactId>
            <version>${taf_version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>test</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <suites>first-suite.xml,second-sute.xml</suites>
            </configuration>
        </plugin>
    </plugins>
</build>
```

<span style="color:#ba3925;">How to define concrete suite xml’s from command line</span>

You are able to define concrete suites names from maven command. Add -Dsuites=first-suite.xml,second-suite.xml parameter into the maven command.

**Example:**

```
mvn package -Dsuites=first-suite.xml,second-suite.xml
```

<span style="color:#ba3925;">How to integrate the taf-maven plugin into a jenkins build</span>

The taf-maven plugin operates in the *test execution phase* of a build, *test analysis* is a separate phase so for this reason this plugin will not
fail your build if tests fail, it just reports the failures. If it is required that the jenkins build is marked as unstable or failed, then a
jenkins plugin will be required to do this, TAF recommend the use of the Jenkins TestNG plugin, usage and configuration details can be found
here https://wiki.jenkins-ci.org/display/JENKINS/testng-plugin

**Note:** Using this plugin will give a yellow ball on a jenkins job if there are test failures, it can also be used to trend the number of test cases executed.

## Change log

<span style="color:#ba3925;">Changes in version 2.0.9.</span>

* initial release

