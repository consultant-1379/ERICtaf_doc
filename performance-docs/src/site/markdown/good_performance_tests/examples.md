<head>
    <title>Examples</title>
</head>

# Good performance tests example

A good example of performance test would be test which utilizes data sources, executes in multiple threads and
reports **performance** metrics to analytics system.

_This example pays attention to data source use, test structure and reporting metrics, however its implementation is left abstract._

For implementation details user can read documentation for [TAF](https://taf.seli.wh.rnd.internal.ericsson.com/)
and [Scenario framework](https://taf.seli.wh.rnd.internal.ericsson.com/scenarios/snapshot/index.html).

[See advanced examples with scenarios](https://taf.seli.wh.rnd.internal.ericsson.com/scenarios/snapshot/index.html#_performance_testing_using_scenarios)

### Start with defining scenario
        @Test
        @TestId(id = "CXX-YYYY", title = "Performance Test Example")
        public void testExample() {
            //Set Execution Id for reporting
            context.setAttribute(ContextKey.EXECUTION_ID, UUID.randomUUID().toString());

            //Build datasource with users and make it shared for parallel scenario
            TestDataSource<MyData> datasource = TafDataSources.filter(fromCsv("data/data.csv", MyData.class), new UsersPredicate(40));
            TestDataSource<MyData> sharedDataSource = TafDataSources.shared(datasource);
            context.addDataSource("data", sharedDataSource);

            //Create scenario with test flow
            TestScenarioBuilder scenarioBuilder = scenario()
                    .addFlow(testflow.someFlowWithLogic())
                    .withVusers(100);

            TestScenarioRunnerBuilder runnerBuilder = runner()
                    .withListener(new LoggingScenarioListener())
                    .withDefaultExceptionHandler(ScenarioExceptionHandler.LOGONLY);

            //schedule scenario to run
            TestScenario scenario = scenarioBuilder.build();
            TestScenarioRunner runner = runnerBuilder.build();
            runner.start(scenario);
        }

### Implement flow with measuring and reporting to analytics system

       public TestStepFlow getSelectTimeFlow() {
               return flow("PerformanceTestFlow")
                       .addTestStep(annotatedMethod(teststeps, "prepareTest"))
                       .addTestStep(annotatedMethod(teststeps, "executeLogic"))
                       .addTestStep(annotatedMethod(teststeps, "reportMetrics"))
                       .addTestStep(annotatedMethod(teststeps, "cleanUp"))
                       .withRampUpTime(5, MINUTES)
                       .withDuration(1, HOURS)
                       .withDataSources(
                               dataSource("data")
                       .build();
           }

In this example test we assume that we test performance for HTTP service:

        @TestStep(id = "prepareTest")
        public void prepareTest() {
            PerformanceOperator operator = provider.get();
            operator.setupHttpTool();
        }

At this step test should perform some business logic which needs to be measured:

        @TestStep(id = "executeLogic")
        public void executeLogic() {
            //record start time
            long executionStart = System.currentTimeMillis();

            PerformanceOperator operator = provider.get();
            operator.executeHttpRequests();

            //record end time
            long executionEnd = System.currentTimeMillis();

            //save to context
            context.setAttribute(ContextKey.METRIC_KEY, executionEnd - executionStart);
        }

Finally get metric from context and report it to analytics system. Clean up resources.

        @TestStep(id = "reportMetrics")
        public void reportMetrics() {
            PerformanceOperator operator = provider.get();
            operator.reportMetricsUsingMetricsBuilder();
        }



