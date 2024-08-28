<head>
   <title>How to provide test results from other sources</title>
</head>

# How to provide test results from other sources

## Issue

In some cases tests may be executed outside of TAF and thus will not use TAF reporting function. There may be value in bringing different
types of test results together for purpose of having a single test report or a single Statement of Verification document.

# Adding Manual Test Results to a report for an Execution Run

Manual test results can now be added to a Test Execution report using Test Campaigns in TMS, combined with TAF scheduler and TAF executor.

Please see [TMS documentation](https://taftm.seli.wh.rnd.internal.ericsson.com/#help/app/tm) for information on setting up Test Campaigns.

Once your Test Campaign is created, you can now add the campaign IDs to the schedule for the execution run.

Please see [Schedule info in TE documentation](https://taf.seli.wh.rnd.internal.ericsson.com/tedocs/latest/schedule.html) for guidelines on adding campaign IDs to schedule.

Once these 2 criteria are met, TAF executor will retrieve the campaign results from TMS and add them to the report for the test execution run.





