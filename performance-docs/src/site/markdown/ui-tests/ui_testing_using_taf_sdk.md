<head>
   <title>UI Testing Using TAF SDK</title>
</head>

# Headless UI Testing with PhantomJS and TAF UI SDK

If you are new to TAF UI testing please refer to
[TAF UI SDK documentation](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#_taf_ui_sdk)
for API and usage examples.

### Configure TAF properties to use Selenium grid

Adding file _hosts.properties.json_ in _resources/taf\_properties_ with **SELENIUM_GRID** type host will
automatically configure TAF framework to perform UI tests on Selenium Grid.

    [
      {
        "hostname": "Selenium hub",
        "ip": "xxx.yyy.zzz.250",
        "type": "SELENIUM_GRID",
        "ports":
            {
                "http": 4444
            }
      }
    ]

### Init PhantomJS browser

Initial step for your test case would be to create new browser with proper type.
TAF provisioned Selenium grid has OS type _**ANY**_ for PhantomJS browsers.

    UI.newBrowser(BrowserType.PHANTOMJS, BrowserOS.ANY);

### Write your test steps logic

Examples for working with _browser_ and _browserTabs_ are available in
[TAF UI SDK Documentation](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#_generic_view_model)


