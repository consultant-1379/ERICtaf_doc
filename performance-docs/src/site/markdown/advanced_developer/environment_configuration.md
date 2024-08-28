<head>
    <title>Environment Configuration</title>
</head>

#Configuring a Performance Monitoring Environment


##Configuring Message Bus for Graphite / Grafana

To update configuration for the Message Bus to which Graphite listens.

* log on to the host with graphite install
* cd /opt/graphite/conf/
* vi carbon.conf

In carbon there is a section for message bus configuration

    # Enable AMQP if you want to receve metrics using an amqp broker
    ENABLE_AMQP = True

    # Verbose means a line will be logged for every metric received
    # useful for testing
    AMQP_VERBOSE = False

    AMQP_HOST = atclvm793.athtem.eei.ericsson.se
    AMQP_PORT = 5672
    AMQP_VHOST = /
    AMQP_USER = root
    AMQP_PASSWORD = shroot
    AMQP_EXCHANGE = eiffel.poc.graphite
    AMQP_METRIC_NAME_IN_BODY = True

##Configuring Message Bus for te-metrics-dwh

To update configuration for Message Bus from which te-metrics-dwh collects data to populate db for Saiku.

* log on to the host with saiku install
* cd /opt/ericsson/te-metrics-dwh/
* service te-metrics-dwh stop
* vi local.properties

        conn.amqp.url=amqp://atclvm793.athtem.eei.ericsson.se
        conn.amqp.exchange=taf.samples
        conn.db.url=jdbc:mysql://localhost:3306/taf_performance?user=saiku_agent&password=password
        db.batchSize=100
        emulator.samples=1000

* Update configuration
* service te-metrics-dwh start

