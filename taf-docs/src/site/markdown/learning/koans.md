<head>
   <title>TAF Koans</title>
</head>

# TAF Koans

TAF Koans is a collection of small exercises, designed to get TAF users up to speed on TAF functionalities.

## What are Koans

Koans consist of a number of broken test cases, and in fixing those tests, increasingly advanced TAF features are learnt. The Koan model provides
very rapid feedback and a structured learning path. As you work through the koans you will learn about data-driven testing, configuration
and dependency injection.

## Pre-requisites

In order to get the most out of these Koans you should have a basic understanding of TAF, and preferably have taken the **TAF Introduction** course
delivered by Ericsson Academy.

## How to get started

The first step is to clone the TAF Koans project.

```
git clone ssh://gerritmirror.lmera.ericsson.se:29418/OSS/com.ericsson.cifwk/ERICtaf_koans
```

Run mvn clean install -DskipTests to download all required dependencies

Import Project into IDE as maven project

## What is in the ERICtaf_koans Project

The Koans themselves can be found in **src/main/java**.

Resources associated with the Koans such as data, taf-properties (datadriven.properties, host.properties etc) can be found in **src/main/resources**.

Additional files that will be needed for the koan can be found in **src/main/java**.

If a koan requires that you modify a file, the location of the file will be in the koan instructions

## Test Ordering

Koans should be performed in the following order:

```
  Data
    * ProvideDataInCSVForTestCase
    * ProvideDataInCSVForAllTestCases
    * ProvideDataInCSVViaURL
    * CreateDataSourceMethod
```

```
  Configuration
    * RetrieveHostInformation
    * UsingSystemProperties
```

```
  Dependency Injection
    * InjectSingletonOperatorIntoTest
    * InjectMultipleOperatorsIntoOperator
```

```
  CLI Tool
      * CLIToolsTestCase
```

```
  UI Tool
      * UiToolsTestCases
```

```
  Scenarios
    * CreateFlow
    * CreateScenario
    * DataSources
    * VUsers
```

## How Koans Work

Each Koan has a set of instructions that need to be followed in order for the Koan to pass. These instructions are located above each Koan.
If you are requested to modify a file there may be further instructions available in the file that is being modified.

```java
    /**
     * This koan is illustrating how to connect data source to test case as easy as it gets
     * We created one for you named "data"
     *
     */
    @Koan
    @DataDriven(name=__)
    @Test
    public void readDataFromCSVFile(@Input("csvData") String dataFromCsv){
        assertNotNull(dataFromCsv);
    }
```java

To get this koan to pass __ needs to be replaced with the data source "data".

```java
    /**
     * This koan is illustrating how to connect data source to test case as easy as it gets
     * We created one for you named "data"
     *
     */
    @Koan
    @DataDriven(name="data")
    @Test
    public void readDataFromCSVFile(@Input("csvData") String dataFromCsv){
        assertNotNull(dataFromCsv);
    }
```java

## Running Koan in IDE
Select koan you want to do eg. ProvideDataInCSVForTestCase and run file as TestNG.
