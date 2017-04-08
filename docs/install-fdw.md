Install Elastic Search FDW
==========================

This installs the foreign data wrapper into postgres and creates the remote table.

Load FDW in Postgres
--------------------

Now the FDW needs to be loaded and configured in Postgres.

First the multicorn extension has to be loaded:

```
echo 'CREATE EXTENSION multicorn;' | psql -p 5432 -h localhost -U postgres postgres
```

Then the Postgres Elastic Search FDW can be loaded:

```
echo "CREATE SERVER multicorn_es FOREIGN DATA WRAPPER multicorn
OPTIONS (
  wrapper 'pg_es.ElasticsearchFDW'
);" | psql -p 5432 -h localhost -U postgres postgres
```

Create Remote Table
-------------------

Then create the remote table:

```
echo "CREATE FOREIGN TABLE elasticsearch_document
    (
        id TEXT,
        title TEXT,
        body TEXT,
        query TEXT
    )
SERVER multicorn_es
OPTIONS
    (
        host 'elasticsearch',
        port '9200',
        index 'wikipedia',
        type 'elasticsearchdocument',
        rowid_column 'id',
        query_column 'query'
    )
;" | psql -p 5432 -h localhost -U postgres postgres
```
