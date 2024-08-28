<head>
   <title>Solutions - HTTP Archive Capture</title>
</head>

# HTTP Archive (HAR) capture

The **HTTP Archive format** or **HAR**, is a JSON-formatted archive file format for logging of a web browser's interaction with a site.

The specification for the HTTP Archive (HAR) format defines an archival format for
HTTP transactions that can be used by a web browser to **export detailed performance data about web pages it loads**.

### HAR Contents

You should expect the HAR file to include a breakdown of timings including:

* How long it takes to fetch the DNS information
* How long each object takes to be requested
* How long it takes to connect to the server
* How long it takes to transfer from the server to the browser of each object
* Whether the object is blocked or not

HAR file can quickly help identify the key performance problems with a web page, which in turn will help efficiently
target your development towards the areas that will deliver the greatest return on your efforts.

TAF allows to integrate HAR file capture with UI tests.
