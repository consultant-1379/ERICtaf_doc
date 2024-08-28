<head>
    <title>Technologies used in TAF Performance Module</title>
</head>

# Technologies used in TAF Performance module

Performance module can be split into 3 logical parts:

* Test Execution
* Results analysis
* Data collection and System Monitoring

## Test Execution
* [TAF Scenario framework](https://taf.seli.wh.rnd.internal.ericsson.com/scenarios/snapshot/index.html) - powerful framework with orchestration mechanism for Performance and End-to-End testing.
* [TAF Test Executor](https://taf.seli.wh.rnd.internal.ericsson.com/tedocs/latest/index.html) - is a distributed system for test schedule running and reporting.
* **PhantomJS Selenium grid** - selenium grid with multiple headless browsers. Allows to execute tests in parallel on different machines.

## Results analysis
* [Saiku](http://wiki.meteorite.bi/display/SAIK/Saiku) - OLAP analysis platform. Integrates with MySQL database.
* [Grafana](http://grafana.org/) - visualization dashboard for time series metrics.
* [MySQL](https://www.mysql.com/) - Database to store metrics.

## Data collection and System Monitoring
* [collectd](https://collectd.org/) - a daemon which collects system performance statistics periodically and provides mechanisms to store the values in a variety of ways.
* [BrowserMob Proxy](https://bmp.lightbody.net/) - capture performance data for web apps (via the [HAR format](https://dvcs.w3.org/hg/webperf/raw-file/tip/specs/HAR/Overview.html)), as well as manipulate browser behavior and traffic, such as whitelisting and blacklisting content, simulating network traffic and latency, and rewriting HTTP requests and responses
* **Metrics builder** - TAF component for publishing metrics into RabbitMQ.

___

### Environment setup
* [Puppet](https://puppetlabs.com/puppet/what-is-puppet) - Puppet is a configuration management system that allows you to define the state of your IT infrastructure, then automatically enforces the correct state. Used for configuring **Test Executor**, **Grafana** and **Saiku** systems.
* [Ansible](http://www.ansible.com/) - software platform for configuring and managing computers. Used for automatic **Selenium Grid** provisioning and configuration.
