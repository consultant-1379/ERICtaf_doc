<head>
   <title>TAF mvn command line parameters</title>
</head>

# TAF mvn command line parameters

TAF has a number of parameters which can be specified at runtime to change the behaviour of the run.

##  -D Parameters

Table of -D Parameters:

Parameter              | Description                                                                            | Example
:--------------------  | :------------------------------------------------------                                | :-----------------------------------
suites                 | A comma separated list of suite files to run.                                          | -Dsuites=suite1.xml,suite2.xml
groups                 | A comma separated list of test groups to run.                                          | -Dgroups=Acceptance,Regression
threadHunt             | When this parameter is 1 TAF will try to join all the threads at the end of the build. | -DthreadHunt=1
skipTests              | Skip the running of all tests (unit tests & TAF tests).                                | -DskipTests
skipTafTests           | Skip the running of the TAF tests (unit tests will still be run).                      | -DskipTafTests
taf.http_config.url    | Load the host information from the url specified.                                      | -Dtaf.http_config.url=http://ess_web/servlets/taf_config
taf.properties.location | Override location of the folder containing configuration files. Default is taf_properties directory in resource folder. | -Dtaf.properties.location=/proj/example/
taf.profiles            | Specify individual profile names under taf_profiles folder in project. Multiple profiles can be seperated by a comma. | -Dtaf.profiles=test,grid,stage,prod
taf_ui.default_browser  | This tells TAF what browser to use by default when running TAF UI tests against a Selenium Grid. More information can be found at: [Loading configuration from HTTP servlet](https://taf.seli.wh.rnd.internal.ericsson.com/userdocs/Latest/index.html#LoadingConfigurationFromHTTPServlet) | -Dtaf_ui.default_Browser=firefox
taf_ui.default_OS       | This tells TAF what OS to use by default when running TAF UI tests against a Selenium Grid. | -Dtaf_ui.default_OS=linux

## -P Profiles

Table of Profiles:

Profile     | Description                                                                  | Example
:---------- | :-----------------------------------------------------------------           | :-----------------------------
skipTestRun | This is a profile to pass to the build to skip the running of the TAF tests. | -PskipTestRun
