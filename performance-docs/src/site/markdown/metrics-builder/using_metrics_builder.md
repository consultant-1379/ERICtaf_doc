<head>
    <title>Using Metrics Builder</title>
</head>

#How to Use Metrics Builder

##Example

Below is an example of how to use Metrics Builder. Metrics builder builds an event based on the values passed into the builder and sends to the Message Bus.

example class which sends the metrics

    public class ReportHelper {
        private static final Logger LOG = LoggerFactory.getLogger(ReportHelper.class);

        public static void reportSuccess(String command, long startOfExecution) {
            long executionTime = System.currentTimeMillis() - startOfExecution;
            LOG.info("Report success metric [{} - {}]", command, executionTime);
            report(command, executionTime, true);
        }

        public static void reportFail(String command, long startOfExecution) {
            long executionTime = System.currentTimeMillis() - startOfExecution;
            LOG.info("Report failure metric [{} - {}]", command, executionTime);
            report(command, executionTime, false);
        }

        private static void report(String command, long executionTime, boolean successful) {
            String executionId = TafTestContext.getContext().getAttribute(ContextKey.EXECUTION_ID);
            long vUser = TafTestContext.getContext().getVUser();

            MetricsBuilder.forCommand(command)
                    .forJob(vUser)
                    .withExecutionId(executionId)
                    .onElements(0)
                    .at(new Date())
                    .gotResponse(new ResponseDto(new ArrayList<AbstractDto>()))
                    .elementsNotCopied(0)
                    .within((int) executionTime)
                    .withElements(0)
                    .successful(successful)
                    .report();
        }
    }

Example test cases using the above implementation

    private void loginUser(String username, String password, AnalysisOperator operator) {
        long startOfExecution = System.currentTimeMillis();
        try {
            operator.login(username, password);
            ReportHelper.reportSuccess("loginUser", startOfExecution);
        } catch (Exception e) {
            ReportHelper.reportFail("loginUser", startOfExecution);
            LOG.error(e.getMessage());
            Throwables.propagate(e);
        }
    }

If you run `MetricsBuilder` multiple times (e.g. multiple Vusers) you probably want all tests to have same execution id and date to be able to group by it in Saiku. To do that before definition of you flow add following lines:

    TestContext context = TafTestContext.getContext();
    context.setAttribute(CmConfigTestSteps.EXECUTION_ID, UUID.randomUUID().toString());
    context.setAttribute(CmConfigTestSteps.EXECUTION_DATE, new Date());

##Visualize Metrics

To see the metrics sent by Metric Builder please see [Saiku Usage](../saiku/saiku_use.html)
