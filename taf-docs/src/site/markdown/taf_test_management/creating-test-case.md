<head>
   <title>TAF Test Management - Creating a Test Case</title>
</head>

# Creating a Test Case

TAF TM allows a user to map out their test cases. During test case creation the following fields should be addressed:

* **Requirement ID(s):** The unique ID of one or more user stories. (e.g. In case of OSS, it is the JIRA ID of the story).

* **Test Case ID:** Global identification of the test case. This value is unique in the TAF TM database.

* **Title:** Title of the test case.

* **Description:** A general description of the purpose of the test case.

* **Pre-Condition:** The description of the test case pre-conditions.

* **Component:** The application/module to be tested.

* **Type:** The Type of the testcase (e.g. Functional, Performance, Workflow, etc).

* **Priority:** The priority of the test case.

* **Group:** Test case group.

* **Context:** Determine which contexts to run the test cases in (UI, REST, CLI, API).

* **Execution Type**: Indicates if the test case is manual or automated.

* **Test Step / Verify Step:** Each test case will have one to many test step/verify step pairs.

* **Status**: State of the design of the test case specification.
