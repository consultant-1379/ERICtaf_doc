<head>
   <title>Solutions - Headless UI Testing</title>
</head>

# Headless UI testing

In order to conduct performance testing of web application with default Selenium browsers (Firefox, Chrome)
you will require a powerful server cluster to host all browser instances.
Resource consumption is the obvious downside of browsers with graphical interface.

A more appropriate approach would be to perform **Headless UI Testing** (testing without graphical interface).

**PhantomJS** is a headless WebKit scriptable with a JavaScript API.
It has fast and native support for various web standards: DOM handling, CSS selector, JSON, Canvas, and SVG.
PhantomJS is a cinch to set up, runs on any machine, and is **_significantly faster_**.

TAF Performance module uses [PhantomJS](http://phantomjs.org/) browsers connected to [Selenium grid](http://www.seleniumhq.org/docs/07_selenium_grid.jsp).

Selenium can control PhantomJS in the same way that it does any other browser.
This gives scalable solution for performing distributed UI testing with high client amount.

