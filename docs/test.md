Testing Performance
===================

Testing the performance of Postgres and Elastic Search can be done by running
equivalent tests against them.

In most cases your searches will return documents in different orders, or
different quantities of documents. This is because Postgres and Elastic Search
perform search in different ways.

Simple Text Search
------------------

Searching for a single word is the simplest test.

### Elastic Search

This searches for `england` and times it too:

```
time curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H 'Content-Type: application/json' -d'
  {
    "query": { "match": { "body": "england" } }
  }
'
```

### Postgres

This searches for `england` (use `\timing` to enable timing within postgres, otherwise prefix it with `time`):

```
echo "SELECT title FROM postgres_document, to_tsquery('england') query WHERE query @@ body_tsvector limit 10;" | psql -p 5432 -h localhost -U postgres postgres
```

The _problem_ with that is it didn't rank the results. This searches for `england` and performs the simplest ranking:

```
echo "SELECT title, ts_rank(body_tsvector, query) AS rank FROM postgres_document_gin, to_tsquery('england') query WHERE query @@ body_tsvector ORDER BY rank DESC LIMIT 10;" | psql -p 5432 -h localhost -U postgres postgres
```

### Postgres FDW

```
echo "SELECT id, title FROM elasticsearch_document WHERE query = 'body:england' LIMIT 10;" | psql -p 5432 -h localhost -U postgres postgres
```

### Results

Postgres GiST Index: 32 minutes

Postgres GIN Index: 15 minutes 45.7 seconds

Elastic Search: 3.6 seconds

Postgres FDW: 683.8 milliseconds

These results are hardly scientific. Caches, Tuning or any other possible
change could've resulted in these specific results. It would be interesting to
know what your results are!
