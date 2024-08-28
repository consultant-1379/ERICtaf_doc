<head>
   <title>TAF Scenarios</title>
</head>

# TAF Scenarios

A Scenario in TAF is a series of Test Steps organized into flows. These are executed against a system under test. A scenario is a series
of actions and verification steps where the order of execution is important. Scenarios allow you to define sequence and scale of flows.
Therefore scenarios are an optimal orchestration mechanism for Performance and End-to-End scenario testing.

A Scenario is designed as a scheduling and orchestration mechanism. The importance of order may come from either flow of data between
steps or the state SUT is traversing.

For example:

To exercise simple email conversation between two customers you would need to:

1. login as User A, select compose mail, fill in all necessary fields to send email to User B, press the send button, logout.

2. login as User B, open received email, press replyButton,  fill in all necessary fields to send email to User A, press the send button, logout.

3. login as User A, open received email, logout.

## Contents

* [Scenarios](taf_scenarios/index.html)
* [Basic Data Concepts](taf_scenarios/basic_data_concepts.html)
* [Data Record](taf_scenarios/data_record.html)
* [Test Steps](taf_scenarios/test_steps.html)
* [Flow](taf_scenarios/flow.html)
* [Building a Scenario Test](taf_scenarios/building_scenario_test.html)
* [Runner](taf_scenarios/runner.html)
* [Extending Scenarios](taf_scenarios/extending_scenarios.html)
* [Vusers](taf_scenarios/vusers.html)
* [Manipulating Data](taf_scenarios/manipulating_data.html)
* [Performance Testing Using Scenarios](taf_scenarios/performance_testing.html)
* [Scenario Debug](taf_scenarios/debug.html)
