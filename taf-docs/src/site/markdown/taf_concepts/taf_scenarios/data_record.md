<head>
   <title>Scenarios - Data Record</title>
</head>

# Data Record

A very important aspect of testing is to have the data disconnected from the test code. It's important to note that environment and configuration
properties are separate from test data and should not be mixed. Test data is information required for actions in the scenario to be performed and
where applicable the expected output of these actions, eg username and password are required to perform login these are test data but URI that opens
login screen is property of SUT

For more information about using data with Taf please see [Basic Data Concepts](basic_data_concepts.html) section.

## Data Record
A Data Record is a single entry of test data information eg row in csv file. It allows you to map a single row of data into an object and work with
this object to get test parameter. This is very beneficial when dealing with a data source that has a lot of parameters, or when dealing with
multiple data sources. A Data Record taken from a data source cannot be modified. It is possible to create a copy of a data record and store it
in a data source or compose new data record from fields. All those operations are possible on test step level, which draws a natural border of usage
of data record. It's more convenient to use POJO on the operator level as it allows full modification of all the fields.

In our example we can specify all information about user as a single data record.

```java
import com.ericsson.cifwk.taf.datasource.DataRecord;

public interface User extends DataRecord {

   String getUsername();
   String getFirstName();
   String getLastName();
   String getEmail();
   String getPassword();
}
```java

This data record can be created from csv file

```
username,firstName,lastName,email,password
ejohsmi,John,Smith,john.smith@email.com,pass
ejandoe,Jane,Doe,jane.doe@email.com,12345
```

or java class

```java
public class UserDataSource {

    @DataSource
    public List<Map<String,Object>> dataSource() {
        List users = new ArrayList();

        Map<String, Object> ejohsmi = new HashMap<String, Object>();
        ejohsmi.put("username","ejohsmi");
        ejohsmi.put("firstName","John");
        ejohsmi.put("lastName","Smith");
        ejohsmi.put("email","john.smith@email.com");
        ejohsmi.put("password","pass");

        Map<String, Object> ejandoe = new HashMap<String, Object>();
        ejohsmi.put("username","ejandoe");
        ejohsmi.put("firstName","Jane");
        ejohsmi.put("lastName","Doe");
        ejohsmi.put("email","jane.doe@email.com");
        ejohsmi.put("password","12345");

        users.add(ejohsmi);
        users.add(ejandoe);

        return users;
```java

There are two ways to retrieve field from data record

* Directly using getter method

```java
user.getUsername();
```java

* Using getFieldValue

```java
user.getFieldValue("username");
```java

## Builder

It is possible to build a data record at runtime.

There are 3 ways you can populate the fields in the data record.

* set an individual field
* set multiple fields using a map
* set multiple fields using datarecord(s)

```java

//...

import com.ericsson.cifwk.taf.datasource.DataRecordBuilder;

//...

  DataRecordBuilder builder = new DataRecordBuilder();

  // Setting individual fields
  final DataRecord dataRecord = builder.setField("username","tom").setField("password","my-password").build();

  // Setting multiple fields using a map
  Map<String, String> credentials = new HashMap<>();
  credentials.put("username","tom");
  credentials.put("password","my-password");

  dataRecord = builder.setFields(credentials);

  //...

  // Setting multiple fields using a data record
  @TestStep()
  public DataRecord login(@Input("user") DataRecord user){
    //...
    DataRecordBuilder builder = new DataRecordBuilder();
    builder.setFields(user);
    builder.setField("isLoggedIn", true);
    return builder.build();
  }

```java


## Transformer

It's possible to transform a DataRecord into a JavaBean and vice versa. This will come in useful for quickly creating new datasources at runtime with user
specific DataRecord types and to reduce bean creation for passing to operator method calls by creating the populated bean from the DataRecord row with one
method call.

**<span style="color:#ba3925;">DataRecord to Bean</span>**

The DataRecord contains method <span style="color:#ba3925;">public &lt;T&gt; T transformTo(Class&lt;T&gt; beanClass)</span>. It is a convenient way to create a bean
with all the data that is stored inside DataRecord object.

```java
UserBean userBean = user.transformTo(UserBean.class);
```java

**<span style="color:#ba3925;">Bean to DataRecord</span>**

Class <span style="color:#ba3925;">BeanTransformer</span> allows to transform Java bean into a DataRecord. Data is transferred based on getter match, e.g. DataRecord’s
method <span style="color:#ba3925;">getUsername()</span> will be populated by information from bean object visible under <span style="color:#ba3925;">getUsername()</span> method.

```java
User userDR = BeanTransformer.transformTo(User.class, userBean);
```java

Here is an example of transforming from a Data Record to a bean and vice versa. Here our data Record is User and our bean is UserBean.

```java
import com.ericsson.cifwk.taf.datasource.DataRecord;
public interface User extends DataRecord {
   String getUsername();
   String getFirstName();
   String getLastName();
   String getEmail();
   String getPassword();
}
```java

```java
public class UserBean {
   private String username;
   private String firstName;
   private String lastName;
   private String email;
   private String password;
   public UserBean(String username, String firstName,
                    String lastName, String email, String password) {
       this.username = username;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.password = password;
   }
   public String getUsername() {
       return username;
   }
   public void setUsername(String username) {
       this.username = username;
   }

   .....

   public String getPassword() {
       return password;
   }

   public void setPassword(String password) {
       this.password = password;
   }
```java

**DataRecord to Bean Transformation**

```java
//Transform user which is DataRecord User to UserBean
UserBean userBean = user.transformTo(UserBean.class);
```java

**Bean to DataRecord Transformation**

```java
//Transform from bean which is of type UserBean to User DataRecord
User userDR = BeanTransformer.transformTo(User.class, userBean);
```java
