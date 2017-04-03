I need to reorg the presentation as it doesn't flow. As such I must start from first principles and focus on the high level before delving.




Text Search

What would it be like if there was no text search?

What is it?

How is it used?

What happens when you do a text search?

How is it implemented?
 - Normalization
 - Stemming

How does it perform matching?
 - Bloom Filter
 - Inverted Index
 - Stop Words

How does it order the results?
 - Term Frequency
 - Field Length Normalization
 - Term Frequency Inverse Document Frequency

How do Postgres and Elastic Search compare?
 - Postgres is significantly more flexible
 - Elastic Search is faster and better

How can I use fast text search with Postgres?
 - FDW make services into tables
 - ES is a service
 - Github link

How can I keep my index up to date?
 - FDW can write
 - Can use triggers
 - ES is not transactional

How can I really keep my index up to date?
 - Be prepared to recover
 - Design your data flows
 - ETL / Kafka / ...
    - talk about how this doesn't have to be a batch operation
    - talk about a consistent ordered log of events
 - Your system contains data and indexes, your data flows keep them in sync

How can I recover from a disaster?
 - Disasters happen
 - Use what you know works

Conclusion
