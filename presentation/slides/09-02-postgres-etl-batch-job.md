POSTGRES-ETL-BATCH-JOB

Define the etl process to generate the elastic search document
You need a way to tie the document back to records in postgres
- the primary key is a great idea because you can make it the elastic search
  document id, which must be unique (putting the same id twice overwrites the
  older with the newer)
