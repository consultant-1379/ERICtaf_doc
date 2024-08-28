<head>
   <title>Dependency Injection</title>
</head>

# Dependency Injection

TAF supports Dependency Injection in tests. It is used to put together all components required for test execution.

TAF supports JSR330 annotations such as @Inject and @Singleton. These are used to mark test components and define their lifecycle.
Dependency Injection works well with operators in TAF.

TAF recommends using the [Provider](http://docs.oracle.com/javaee/6/api/javax/inject/Provider.html) design pattern to "inject" the required operator or operators. Provider has important advantages over other methods
of obtaining operators.

* It works well with vUsers, by using the operator in this way you can ensure that a new instance of the operator is always provided for each vUser.

* Provider can be used to get both stateful or stateless operators.

To start using this feature, you create a new operator which should then be annotated with `@Operator`. When you annotate the operator with `@Operator`
you are making this particular operator discoverable via classpath scanning.

The next step is to tell TAF to provide the operator instance. To do so you have to inject Provider with the operator parameter. Next
you call the get method inside the test body to get an active instance of the operator.

In the example below, we activate the given operator:

```java
    package injection;

    import org.testng.annotations.Test;

    import javax.inject.Inject;
    import javax.inject.Provider;

    public class OperatorTest {

        @Inject
        Provider<Operator> provider;

        @Test
        public void shouldProvideOperator() {
            Operator operator = provider.get();
        }

    }
```
