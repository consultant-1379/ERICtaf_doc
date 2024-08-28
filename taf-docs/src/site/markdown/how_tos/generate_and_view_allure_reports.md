<head>
   <title>How to generate and view Allure reports</title>
</head>

# How to generate and view allure reports

Jcat used to be the reporting mechanism for TAF but is now removed and no longer supported. TAF now uses Allure reporting and requires
testware to have a dependency on the allure-runner module or other SDK's (i.e. all-taf-sdk, tor-sdk) in order to work correctly.
Below shows the steps needed to use Allure when running tests locally.

## Suggested Changes to Testware

Changes based on archetype used to generate:

  * Taf-Archetype 3.0.9
      + No changes needed

  * Taf-Archetype 3.0.5
      + Remove references to taf-allure-maven-plugin and taf-allure-old-maven-plugin in your pom files.
      + Remove the two profiles maven305 and maven323

## Software Needed

  * You will need to download and install the allure-cli app as described [here](http://wiki.qatools.ru/display/AL/Allure+Commandline)

## Generating Report

  * After running your tests, from the root directory of your testware execute the command:
      + *allure generate &lt;testware folder&gt;/target/allure-results*
      + You can use the "-o" flag when you want to save to specific location. Just add "-o &lt;testware folder&gt;/target/allure-report"
        to the previous command

  * To **view the report** you have two options:
      + Open index.html located in **&lt;testware folder&gt;/target/allure-report** with Firefox (or where you specified with -o)
      + Execute _allure report open_ from the root directory of your testware testware
