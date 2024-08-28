<head>
   <title>Useful guides not under TAF umbrella</title>
</head>

# Useful guides not under TAF umbrella

These documents contains useful information, that we put here for convienience. Please note they are not part of TAF.

# TAF Environment Setup

Here you can find the environment set up instructions to start with TAF.

## Environment - Eclipse, Maven, GIT

TAF project is a [Maven](http://maven.apache.org/what-is-maven.html)project.

There are a number of tools you need to have on your machine in order to work with TAF:

* Eclipse E4e (Ericsson's version of Eclipse) available here: [Click here](https://ericoll.internal.ericsson.com/sites/EECS/Pages/EECSEclipse.aspx)

* Maven

* Maven Plugin for Eclipse

* Groovy Plugin for Eclipse

* TestNG Plugin for Eclipse

There are further optional tools you can have (such as EGit plugin for Eclipse or Maven Apache server), but the above is the minimal amount needed to get your TAF project working.

:-------  | :-----------------------------
**NOTE:** | For help setting up Maven, go to the Maven site http://maven.apache.org/download.cgi#Installation, or contact your CI Execution team.

TAF provide a clean version of Eclipse that has all the required plugins to get started [here](https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/service/local/repositories/prototype/content/com/ericsson/cifwk/taf/taf_eclipse/4.3.0/taf_eclipse-4.3.0.zip) (for Windows 64bit).
Just download the zip, extract it, and if you have Maven installed correctly, you are ready to go.

If you would rather install the plugins yourself, there is info below.

## Maven, Groovy, TestNG plugins

The Maven m2e, groovy and testNG plugins for Eclipse should all be available at the internal Ericsson eclipse update site.

In Eclipse, go to Help-> Install new software-> Work with - then from the dropdown choose the Ericsson eclipse update site that matches your version of Eclipse. From here you should find the plugins needed.

If you cannot find the plugins, they should also be available in the Eclipse marketplace.

<span style="color:#ba3925;">Other Installations</span>

Within your team you may need the following (please check with your team first):

* Git –> For help with Git you can contact your CI Execution team or go to the Git website (http://git-scm.com/)

* JBoss Application server – depending on your project you may use JBoss. Please check with your team.

  * The CM Mediation Team have created a good setup with pre-written standalone file that will provide a quick setup of JBoss.

  * Good JBoss Setup from CM Mediation Team [Click here](https://confluence-oss.seli.wh.rnd.internal.ericsson.com/display/TLT/TOR+CM+Mediation+Local+Depolyment)

**NOTE:** This is not managed by TAF.
