<head>
   <title>Code snippet for using CLI</title>
</head>

# Code snippet for using CLI

```java
import com.ericsson.cifwk.taf.data.Host;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CLIShellTest {

    public static final String HOST_FOLDER = "/usr/tmp/";

    Shell shell;

    @Before
    public void setUp() throws Exception {
        Host host = DataHandler.getHostByName("sc1");
        CLI cli = new CLI(host);
        shell = cli.openShell(Terminal.VT100);
    }

    @After
    public void tearDown() throws Exception {
        if (shell != null) shell.disconnect();
    }

    @Test
    public void helloWorldTest() throws Exception {
        shell.writeln("echo 'Hello World!'");
        shell.expect("Hello World!");
        shell.writeln("echo \"Hello from TAF CLI Tool\"");
        shell.expect("TAF CLI Tool");
        shell.writeln("exit");
        shell.expectClose();
        assertTrue(shell.isClosed());
    }

    /**
     * Execute shell script: <code>t1.sh</code>
     * <pre>
     * #!/bin/bash
     * echo "Enter text:"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t1() throws Exception {
        shell.writeln(HOST_FOLDER + "t1.sh");
        shell.expect("Enter text:");
        shell.writeln("exit");
        shell.expectClose();
        assertTrue(shell.isClosed());
    }

    /**
     * Execute shell script: <code>t2.sh</code>
     * <pre>
     * #!/bin/bash
     * echo "Enter text:"
     * read  text
     * echo "Text:$text"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t2() throws Exception {
        shell.writeln(HOST_FOLDER + "t2.sh");
        shell.expect("Enter text:");
        shell.writeln("T2_TEST_MESSAGE");
        shell.expect("Text:T2_TEST_MESSAGE");
        shell.writeln("exit");
        shell.expectClose();
        assertTrue(shell.isClosed());
    }

    /**
     * Execute shell script: <code>t3.sh</code>
     * <pre>
     * #!/bin/bash
     * read -p "Enter text:" text
     * echo "Text:$text"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t3() throws Exception {
        shell.writeln(HOST_FOLDER + "t3.sh");
        shell.expect("Enter text:");
        shell.writeln("T3_TEST_MESSAGE");
        shell.expect("Text:T3_TEST_MESSAGE");
        shell.writeln("exit");
        shell.expectClose();
        assertTrue(shell.isClosed());
    }

    /**
     * Execute wrong command and check exit code
     */
    @Test
    public void testExitValue() throws Exception {
        shell.writeln("wrong_command");
        try {
            System.out.println(shell.getExitValue());
            fail();
        } catch (Exception ignore) {
        }
        shell.writeln("exit");
        shell.expectClose();
        assertTrue(shell.isClosed());
        assertEquals(127, shell.getExitValue());
    }

}
```java

**<span style="color:#ba3925;">CLI Execute command Examples</span>**

```java
import com.ericsson.cifwk.taf.data.Host;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CLIExecuteCommandTest {

    public static final String HOST_FOLDER = "/usr/tmp/";

    CLI cli;
    Shell shell;

    @Before
    public void setUp() throws Exception {
        Host host = DataHandler.getHostByName("sc1");
        cli = new CLI(host);
    }

    @After
    public void tearDown() throws Exception {
        if (shell != null) shell.disconnect();

    }

    @Test
    public void helloWorldTest() throws TimeoutException {
        shell = cli.executeCommand(Terminal.VT100, "echo \"Hello World!\"");
        shell.expect("Hello World!");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }

    /**
     * Execute shell script: <code>t1.sh</code>
     * <pre>
     * #!/bin/bash
     * echo "Enter text:"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t1() throws TimeoutException {
        shell = cli.executeCommand(Terminal.VT100, HOST_FOLDER + "t1.sh");
        shell.expect("Enter text:");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }


    /**
     * Execute shell script: <code>t2.sh</code>
     * <pre>
     * #!/bin/bash
     * echo "Enter text:"
     * read  text
     * echo "Text:$text"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t2() throws TimeoutException {
        shell = cli.executeCommand(Terminal.VT100, HOST_FOLDER + "t2.sh");
        shell.expect("Enter text:");
        shell.writeln("T2_TEST_MESSAGE");
        shell.expect("Text:T2_TEST_MESSAGE");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }

    /**
     * Execute shell script: <code>t3.sh</code>
     * <pre>
     * #!/bin/bash
     * read -p "Enter text:" text
     * echo "Text:$text"
     * </pre>
     */
    @Test
    public void expectRemoteExecution_t3() throws TimeoutException {
        shell = cli.executeCommand(Terminal.VT100, HOST_FOLDER + "t3.sh");
        shell.expect("Enter text:");
        shell.writeln("T3_TEST_MESSAGE");
        shell.expect("Text:T3_TEST_MESSAGE");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }

    /**
     * Execute commands chain
     */
    @Test
    public void expectRemoteExecution_string_multiple_command_as_one_execute() throws Exception {
        shell = cli.executeCommand(Terminal.VT100, "read -p \"Enter text:\" text ; echo
        \"Text:$text\"");
        shell.expect("Enter text:");
        shell.writeln("T3_TEST_MESSAGE");
        shell.expect("Text:T3_TEST_MESSAGE");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }

    /**
     * Execute commands chain
     */
    @Test
    public void expectRemoteExecution_multiple_command_as_one_execute() throws Exception {
        shell = cli.executeCommand(Terminal.VT100,
                                   "read -p \"Enter text:\" text",
                                   "echo \"Text:$text\"");
        shell.expect("Enter text:");
        shell.writeln("T3_TEST_MESSAGE");
        shell.expect("Text:T3_TEST_MESSAGE");
        shell.expectClose();
        assertEquals(shell.getExitValue(), 0);
        assertTrue(shell.isClosed());
    }

    /**
     * Execute wrong command and check exit code
     */
    @Test
    public void shouldReturnExitValue_whenProcessFinished() throws TimeoutException {
        shell = cli.executeCommand(Terminal.VT100, "wrong_command");
        assertEquals(127, shell.getExitValue());
        //
        shell = cli.executeCommand(Terminal.VT100, "wrong_command","echo 'exitcode:'$?");
        shell.expect("exitcode:127");
        assertEquals(0, shell.getExitValue());
    }
}
```java
