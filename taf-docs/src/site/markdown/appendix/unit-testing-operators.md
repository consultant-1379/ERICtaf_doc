<head>
   <title>Unit testing your operator</title>
</head>

# Unit testing your operator

Like any other API, an operator should be unit tested. Any external interfaces should be mocked.

## Maven configuration

In order to execute unit tests in your operator module you will need to update the pom with the maven-surefire-plugin

```java
<plugin>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>2.16</version> <!-- Double check http://mvnrepository.com for version --!>
  <dependencies>
    <dependency>
      <groupId>org.apache.maven.surefire</groupId>
      <artifactId>surefire-junit47</artifactId>
      <version>2.16</version> <!-- Double check http://mvnrepository.com for version --!>
    </dependency>
  </dependencies>
```java
