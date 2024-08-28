<head>
   <title>Scenarios - Introduction</title>
</head>

# Scenarios

## How does it differ from Feature Integration Test Case

The Goal of a feature integration test case is to verify correctness of behaviour of SUT from perspective of single component, and leave SUT
unchanged. For example we could verify sending an email feature as a test case. A precondition would be to have a user logged in.

Scenarios are designed to verify series of user actions to achieve customers goals regardless of components or business logic boundaries, like in the example above.

## Defining a scenario

A vital point of a scenario is its goal, what customer task will be fulfilled by the series of actions defining the scenario. Once you have
determined the action to be performed one should breakdown the action into test steps.

### Breakdown action into test steps

A good practice is to have testSteps atomic so they can be reused as building blocks of more complex flows. We can break the above example
into following steps:

**Flow 1**

User A

* login
* select mail action - compose
* set mail fields
* send email
* logout

**Flow 2**

User B

* login
* open mail folder - received
* open mail
* select mail action - reply
* set mail fields
* send email
* logout

**Flow 3**

User A

* login
* open mail folder - received
* open mail
* logout

It is easy to notice that many of steps are duplicated.

Now that we have broken down our scenario into TestSteps we must consider the data that will be passed to each step.

### Data required

For the previous scenario we would need the following data sources

* Users with at least 2 entries - Used by login/logout steps and compose mail steps

* Mail contents with at least 2 entries - Used by compose mail step and verification of mail folder content

There are two scopes of data sources available to flow:

* Flow specific, used only by this flow and containing data not available on system under test before execution. Specified on
  flow and only available for lifecycle of this flow. In this case MailContents data source

* Global data source with data previously used on system under test. Specified externally to flow and available to all flows.
  In our case it is +users+ data source. The best practice is to reuse data source created by another scenario eg Add User to System.

For more information: See **Basic Data Concepts** section.

### Data produced

In our example we are reusing Users data created by another scenario. Also we are producing data - sent emails. It can be reused in other
scenarios, for example delete mail. This way the data flow through the system is more realistic and provides better test coverage.

:------------- | :------------
**IMPORTANT:** | Analysis of data flow is also important from performance perspective. The amount of users can impact time required to login to the system. Also large amount of received emails will make load time bigger.

## User Session

User session reflects the information about status of SUT impacted by interacting with user. For example it would store browser status or user name.
It is good practice to set scenario boundaries when the user session needs to be restarted.

For more information: See **Vusers** section.

## Data Flows

It is not recommended to pass modified data between steps, if data is persistently changed on SUT the best practice is to split flow and populate
and reuse data source. It is perfectly fine to reuse the same data record between all the test steps in the flow.

Here is an example of how some data sources could be used in different test steps


Test Step        | Input Data Source| Parameters        | Data
:---------       | :-----------     | :--------------   | :----------
login            | Users            | username,password | user1
set mail fields  | Users            | emailAddress      | user2
                 | MailContent      | subject, body     | mail1
login            | Users            | username,password | user2
open mail        | Users            | emailAddress      | user1
                 | SentMail         | subject           | mail1
set mail fields  | Users            | email             | user1
                 | MailContent      | subject, body     | mail2
login            | Users            | username,password | user1
open mail        | Users            | emailAddress      | user2
                 | SentMail         | subject           | mail2

