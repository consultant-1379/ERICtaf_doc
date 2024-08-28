<head>
   <title>Solutions - DDC</title>
</head>

# Diagnostic Data Collection

Diagnostics Data Collection (DDC) is a collection of scripts and utilities which perform periodic system analysis and dimensioning of the running system.
DDC is responsible for collecting data throughout the day, and collating the data into a single gzipped tar file at the end of the day.
Historical data for the previous 28 days is maintained, and data older than this is removed from the system.
