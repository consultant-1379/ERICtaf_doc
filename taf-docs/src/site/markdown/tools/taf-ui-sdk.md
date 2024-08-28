<head>
   <title>TAF UI</title>
</head>

# What is TAF UI?

TAF UI is a software development kit for creating tests for UI elements (primarily Web pages). It abstracts the details
of underlying implementation and provides a unified interface to work with UI that has API access (Web, SWT). 

It also supports work with abstract UI applications, and is suitable for testing desktop applications which do not provide an API access.

TAF UI is based on [UI Test Toolkit framework] (https://taf.seli.wh.rnd.internal.ericsson.com/ui-test-toolkit/), 
introducing just a thin integration layer to ease the work for TAF users. 

# API

TAF UI API is the same as UI Test Toolkit API, with just one exception: 
the primary entrypoint is `com.ericsson.cifwk.taf.ui.UI`, not `com.ericsson.cifwk.taf.ui.UiToolkit`.

# Project setup

Create a TAF-based test project. All needed TAF UI classes will be available to you out of the box.

Make sure you have the following dependencies:

For Web tests:

```
    <dependency>
     <groupId>com.ericsson.cifwk</groupId>
     <artifactId>ui-web-taf-sdk</artifactId>
     <version>${taf_version}</version>
     <type>pom</type>
    </dependency>
```

For SWT tests:

```
    <dependency>
     <groupId>com.ericsson.cifwk</groupId>
     <artifactId>ui-swt-taf-sdk</artifactId>
     <version>${taf_version}</version>
     <type>pom</type>
    </dependency>
```

For all types of UI tests:

```
    <dependency>
     <groupId>com.ericsson.cifwk</groupId>
     <artifactId>all-taf-sdk</artifactId>
     <version>${taf_version}</version>
     <type>pom</type>
    </dependency>
```

# TAF UI Web

## Default browser settings

`Browser newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)` is an ultimate signature. If one of the parameters is omitted (other signatures are used), UI will look for the default settings:

* `taf_ui.default_browser` - default browser type. Should be set to one of the values from `com.ericsson.cifwk.taf.ui.BrowserType`

* `taf_ui.default_OS` - default OS where the browser should be launched. Should be set to one of the values from `com.ericsson.cifwk.taf.ui.BrowserOS`

You can define these settings in a property file or pass via command line when starting UI tests.

* if `newBrowser()` is called, all browser parameters will have the default values.

* if `newBrowser(BrowserType browserType)` is called, browser OS will have the default value.

* if `newBrowser(BrowserType browserType, BrowserOS os)` is called, no parameters will have the default values. Version should always be defined explicitly.

* if `newBrowser(BrowserType browserType, BrowserOS os, String browserVersion)` is called, no parameters will have the default values.

The [default UI Test toolkit browser properties](https://taf.seli.wh.rnd.internal.ericsson.com/ui-test-toolkit/03_browsers.html#Default_browser_settings) will work, too.

## Selenium Grid usage 

TAF UI SDK provides an opportunity to open browsers remotely. This is based on [Selenium Grid 2](https://code.google.com/p/selenium/wiki/Grid2) technology.

To create and use an instance of a remote browser, you have to do one of the following:

1. Define HTTP connection details for Grid hub (master node) in hosts property file. You have to set the <span style="color:#ba3925;">SELENIUM_GRID</span> host type for this. Browser requests will be sent to the first grid found in hosts property file.
2. Define the following TAF properties: `taf_ui.default_grid_ip` and `taf_ui.default_grid_port`. The default [UI Test toolkit grid properties](https://taf.seli.wh.rnd.internal.ericsson.com/ui-test-toolkit/03_browsers.html#Remote_browsers) will work, too.

If none of this is present, tests will run locally.

See [more details on UI tests on the grid](https://taf.seli.wh.rnd.internal.ericsson.com/ui-test-toolkit/03_browsers.html).


# TAF UI SWT
 
## How to deploy TAF SWT Agent into target OSGi container

Execute code line:

```java
    new SwtNavigator(DataHandler.getHostByName("master"), "/opt/ericsson/nms_cex_client/bin/cex_client","/opt/ericsson/nms_cex_client/bin/cex_client_application.ini","192.168.0.86:0.0",300000L);
```

**Example hosts.properties file**

```
	host.properties example:
	host.master.ip=atvts953.athtem.eei.ericsson.se
	host.master.port.ssh=2205
	host.master.user.root.pass=
	host.master.user.root.type=admin
	host.master.user.nmsadm.pass=
	host.master.type=rc
```

## How to create an instance of appropriate window

```java
    DesktopNavigator desktopNavigator = UI.newSwtNavigator(getRemoteHost());
    DesktopWindow window = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid
    configuration");

    private Host getRemoteHost() {
            DataHandler.getHostByName("master")
    }

    private Host getRemoteHost() {
            Host host = new Host();
            host.setIp("atvts953.athtem.eei.ericsson.se");
            Map<Ports, String> map = Maps.newHashMap();
            map.put(Ports.HTTP, "10001");
            host.setPort(map);
            return host;
    }
```

See [SWT testing guidelines] (https://taf.seli.wh.rnd.internal.ericsson.com/ui-test-toolkit/05_swt.html).


# How should I close the resources after test?

You don't have to worry about that: all UI windows, opened in tests, are automatically closed after the tests are finished. This is handled
by `UiTestEventSubscriber`, which is a part of TAF UI. However, you can use `UI.closeWindow(TestExecutionEvent eventType)`
to override the default functionality, calling it in the test method, annotated with `@BeforeSuite` annotation.

Windows can be closed in the following cases:

:------------------- | :----------------------
ON_TEST_FINISH       | each time a test finishes
ON_SUITE_FINISH      | after the SuiteRunner has run all the test suites
ON_EXECUTION_FINISH  | on completion of execution

Example:

```java
@BeforeSuite
public void beforeSuite() {
    UI.closeWindow(TestExecutionEvent.ON_SUITE_FINISH);
}
```

Here is a sample console output showing the order of execution:

```
15:25:16,418 [INFO] [com.ericsson.test.UiTest] Test Running
15:25:16,594 [TRACE] [com.ericsson.cifwk.taf.ui.spi.UiTestEventSubscriber] On Test Execution finish Close all windows
15:25:16,601 [INFO] [com.ericsson.test.UiTest] AfterMethod
15:25:16,605 [TRACE] [com.ericsson.cifwk.taf.ui.spi.UiTestEventSubscriber] On Suite Execution finish Close all windows
15:25:16,609 [INFO] [com.ericsson.test.UiTest] AfterSuite
15:25:16,698 [INFO] [com.ericsson.cifwk.taf.ui.spi.UiTestEventSubscriber] onSuiteFinish

===============================================
Ui Test Suite
Total tests run: 1, Failures: 0, Skips: 0
===============================================

15:25:16,803 [TRACE] [com.ericsson.cifwk.taf.ui.spi.UiTestEventSubscriber] On Execution finish Close all windows
```

Also, you can call `UI.closeAllWindows()`. It will close all browser windows.

## Manual window closing in Scenarios

From test framework perspective the [Scenario](https://taf.seli.wh.rnd.internal.ericsson.com/scenarios-rx/) is just a test. 
So by default all browsers opened during the execution of scenario will be open until the test containing scenario finishes.
You can configure auto-closing to happen less frequently (see the chapter above), 
but if you want to close browsers more frequently, you should do it manually.

There is a recommended way to close browsers in scenarios if you are not happy with the default behaviour 
(i.e., want to close browsers more frequently than after each `@Test`): close them in the same flow where they were opened.

To make sure the browser is closed, put the logic to close it into the test step that always runs, after logout.
 
### Example for RX scenarios

Test steps:

```
@TestStep(id = LOGIN)
public DataRecord login() {
    Browser browser = operator.login("username", "password");
    return new DataRecordBuilder()
             .setField("browser", browser) // value will be put into context
             .setField("browserTab", browserTab) // value will be put into context
             .build();
}

@TestStep(id = DO_STUFF)
public void doStuff(@Input("browser") Browser browser, @Input("browserTab") BrowserTab browserTab) { // Injection from context
    // ....
}

@TestStep(id = LOGOUT)
public void logout(@Input("browserTab") BrowserTab browserTab) { // Injection from context
    doLogout(browserTab);
}

@TestStep(id = CLOSE_BROWSER)
public void logout(@Input("browser") Browser browser) { // Injection from context
    browser.close();
}
```

Scenario:

```
RxScenario scenario = scenario()
    .addFlow(flow("Main Flow")
            .addTestStep(annotatedMethod(this, LOGIN))
            .addTestStep(annotatedMethod(this, DO_STUFF))
            .addTestStep(annotatedMethod(this, LOGOUT))
            .addTestStep(annotatedMethod(this, CLOSE_BROWSER)).alwaysRun()
    )
    .build();
```

### Example for ENM TAF scenarios

In ENM there's a shared test library `enm-security-test-library` that already contains operators, test steps and flows 
for operations with browsers. 

Predefined test steps `LoginLogoutUiTestSteps.login()` and `LoginLogoutUiTestSteps.close()` available in this library 
create and destroy the browser instance (available in `@Inject`able `TafToolProvider` after login). 

Scenario:

```
Scenario scenario = scenario()
    .addFlow(flow("Main Flow")
            .addTestStep(annotatedMethod(this, TEST_STEP_LOGIN))
            .addTestStep(annotatedMethod(this, DO_STUFF))
            .addTestStep(annotatedMethod(this, TEST_STEP_LOGOUT))
            .addTestStep(annotatedMethod(this, TEST_STEP_CLOSE)).alwaysRun()
    )
    .build();
```