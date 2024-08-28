<head>
   <title>Setting up Jenkins to use HTML Publisher Plugin</title>
</head>

# Setting up Jenkins to use HTML Publisher Plugin

## CI Framework TAF - Jenkins HTML Publisher

[Back to the TOR_CI_Framework_Team Wiki page](http://atrclin2.athtem.eei.ericsson.se/wiki/index.php/TOR_CI_Framework_Team)

The purpose of this wiki is to outline how to install/use the HTML Publisher Plugin in Jenkins such that it is possible to
link it to a webpage created during the running of a Jenkins job into the homepage for that job.

### Check if `HTML Publisher Plugin' is already installed

* From the Jenkins home page

### If 'HTML Publisher Plugin' is not listed then install the plugin

by doing the following:

* Click on the 'Available' tab

* Search for `HTML Publisher Plugin', click on the checkbox to mark it for install

* Click on 'Download now and install after install'

* On the next page that pops up - click on the checkbox 'Restart Jenkins when installation is complete and no jobs are running'

### Add the plugin to the Jenkins job

* From the Jenkins home page, navigate to the job page and click 'Configure'

* Go to the 'Post Build Actions' which will be towards the bottom of the page

* Click on the checkbox for 'Publish HTML reports'

* This will enable a button to add a HTML directory - click this button

* Enter the folder and webpage to publish

* Run the job again for the changes to enabled
