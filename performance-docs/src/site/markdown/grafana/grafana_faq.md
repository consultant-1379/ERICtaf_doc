<head>
    <title>Grafana FAQ</title>
</head>

#Grafana FAQ

###Can I see metrics update in real time?

Yes. Grafana continuously monitors the message bus to retrieve metrics and populate your dashboard.

###How does Grafana retrieve the metrics?

Grafana is built on top of Graphite which monitors the message bus for events/messages.
Graphite configuration specifies the message bus and exchange / queue.

###How can I publish metrics to Grafana?

Metrics are sent to Graphite/Grafana through the message bus.
CPU and Memory metrics can be sent using DDC, please see [DDC](grafana_ddc.html).
Custom metrics can be sent from testware using Eiffel.

###Can I save my report?

Only the Metrics Dashboard can be exported to a file.
Please see [user docs](http://docs.grafana.org/reference/export_import/)

###Can I view historic data in Grafana?

No, Grafana does not store data for historical information or trend analysis.
