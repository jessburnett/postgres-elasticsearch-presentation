POSTGRES-ETL-KAFKA

Can use pg bottled water.
https://github.com/confluentinc/bottledwater-pg

Allows you to represent a table as a compacted kafka topic.
Messages are written in AVRO format and a schema registry must be running.
Need to write a consumer for the topic that will write to elasticsearch.
The consumer is able to write the entire current state of the table (based on how compacted topics work).
This provides timely updates and the ability to completely restore the index.
