Data Loader
===========

This is a java application that can load the Wikipedia XML into Elastic Search
and Postgres. This requires Java 8.

Wikipedia XML
-------------

This was tested with `enwiki-20161020-pages-articles.xml`. This is a 56GB dump
of every article from the English language Wikipedia.

You can find this [here](https://en.wikipedia.org/wiki/Wikipedia:Database_download#English-language_Wikipedia).

Usage
-----

This _does not exit when finished_. If you download
`enwiki-20161020-pages-articles.xml` then you should have 17,008,269 documents
in both Elastic Search and Postgres when the load is complete.

```
cd pg-es-xml-loader
./mvnw clean package
java -jar target/*.jar enwiki-20161020-pages-articles.xml
```

This uses maven wrapper to handle downloading maven and all dependencies. If
you have maven 3 already installed you can just use that.

You can use the following commands to test the number of loaded documents.

For Postgres:
```
echo "select count(1) as pg_count from postgres_document" | psql -p 5432 -h localhost -U postgres postgres
```

The output when loading has completed looks like:
```
 pg_count
──────────
 17008269
(1 row)
```


For Elastic Search:
```
curl 'http://localhost:9200/_cat/indices?v'
```

The output when loading has completed looks like:
```
health status index     pri rep docs.count docs.deleted store.size pri.store.size
yellow open   wikipedia   5   1   17008269            0     80.3gb         80.3gb
```

Next Steps
----------

Now you need to index the postgres table. See the instructions [here](index-postgres.md)
