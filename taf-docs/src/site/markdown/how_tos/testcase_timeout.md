<head>
   <title>How to set a timeout on a test case</title>
</head>

# How to set a timeout on a test case

## Issue

Sometimes a testcase may hang or run beyond the expected execution time and this can cause a problem if there is a time limit on the suite that contains this testcase.

## The solution

It is considered good practice to set a timeout on each testcase so that the above mentioned issue does not arise and cause instability in the test execution run.

The simplest way to set the timeout is to use the `@TestOptions` annotation where you pass a value for the `timeout` property. Here is an example where we are setting a timeout of 5 seconds.

```java
@Test
@TestOptions(timeout = "5000"})
public void mySimpleTestCase(){
    ...
    ...
    }
```

It is also possible to parameterize this timeout using the `@TestOptions` annotation, please see the section called `Test Properties` in
[TAF configuration](../taf_concepts/taf-configuration.html#test_properties) for more details on how to do this.
