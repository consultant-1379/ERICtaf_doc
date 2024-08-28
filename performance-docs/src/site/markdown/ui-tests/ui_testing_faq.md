<head>
   <title>UI Testing FAQ</title>
</head>

# Headless UI Testing FAQ

### What is headless UI testing?

Headless UI Testing - is a testing without graphical interface.

### What is [TAF UI SDK](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#_taf_ui_sdk)?

TAF UI SDK is a software development kit for creating tests for UI elements (primarily Web pages).
It abstracts the details of underlying implementation and provides a unified interface to work with UI that has API access (Web, SWT).
It also provides an image recognition tool DesktopNavigator that allows for work with abstract UI applications,
and is suitable for testing desktop applications which do not provide an API access.

### What is [PhantomJS](http://phantomjs.org/)?

**PhantomJS** is a headless WebKit scriptable with a JavaScript API.
It has fast and native support for various web standards: DOM handling, CSS selector, JSON, Canvas, and SVG.
PhantomJS is a cinch to set up, runs on any machine, and is **_significantly faster_**.

### What is [Selenium Grid]()?

**Selenium Grid** is part of the Selenium project.
It lets you distribute test execution across several machines.
You can connect to it with Selenium RemoteWebDriver by specifying the browser, browser version, and operating system you want.

Grid consists from:

* Single **selenium hub** which distributes execution and manages sessions;
* One or more **selenium nodes**. The node runs the browser,
and executes the Selenium commands within that browser against the application under test.


### I want to measure time of my page load. Is this possible?

Yes, for this you can use [HAR Capture](../har/http_archive_capture.html).
This will create archive with detailed information breakdown of each HTTP action performed on your page.

### What should I test with headless UI testing?

Consider headless UI testing if you plan to execute test cases with high level of parallelism and don't have much hardware resources.

For performance testing it is recommended to perform direct HTTP requests to the server and measure response times.
This allows to produce higher load on server with less resource consumption.

### Why my browser actions are so slow?

Executing UI tests with Selenium brings significant overhead.
Let's review simple case when user wants to test login performance with Selenium.
User writes test consisting from test steps:

* open browser
* open page
* enter credentials
* press submit
* check result

1. All commands are sent through Selenium Hub, which identifies node by session id and forwards request to Selenium Node.
2. Each _**browserTab.getView(...)**_ is also command to Selenium Hub/Node.
3. Node constructs response with DOM state and sends it back to Selenium Hub which forwards it to the client (you) upon state request.
4. Add network latency to all requests.

Thus, measuring time for login operation will not give actual time of server performance.
Time will consist from Selenium grid performance, network performance and your server response time.

