<head>
    <title>Grafana Usage</title>
</head>

#Grafana Usage

##Accessing Grafana

Grafana is available on port 8084 of the performance vApp.

Replace XXXX with vApp hostname where Saiku is installed - atvtsXXXX.athtem.eei.ericsson.se:8084

![Grafana](../images/grafana_unconfigured.png)

##Configuring Grafana

Once you access the webpage, you can now configure Grafana to visualize your performance metrics.

First you must add a row, this is a place holder / container for panels (graphs or text).

![Add Row](../images/grafana_add_row.png)

Once you add the row, there will be 2 options available via blue and green tabs on the left.
Blue tab allows you to rename.
Both provide options to configure the row, by adding panels (graphs or text), size, etc)

![Configure Row](../images/grafana_blue_green_tabs.png)

Next add a graph to your row and select edit
![Edit Graph](../images/grafana_edit_graph_1.png)

In here you have the options to make general update, such as name, size, etc.
![Configure Graph](../images/grafana_edit_graph_2.png)

To specify the metrics you want to monitor select the metrics tab and select the metrics in question in the query builder.
![Select Metrics](../images/grafana_edit_graph_3.png)

##Example Grafana Page

Here is an example of what can be visually achieved with Grafana.
![grafana_example](../images/grafana_demo.png)

[Live Demo](http://play.grafana.org/)
