Search Test with Postgres and Elastic Search
============================================

Wikipedia XML
-------------

This was tested with `enwiki-20161020-pages-articles.xml`. This is a 56GB dump
of every article from the English language Wikipedia.

Docker Compose
--------------

Docker Compose is used to run both Elastic Search and Postgres. This means you
can start and stop both, together.

They save their data in a `data` folder in the root of the project. The files
created by docker are usually owned by root.

### Starting

```
docker-compose up -d
```

The `-d` flag detaches you, which means you don't see the log output.

You can exclude it to see the logs. If you are not detached then pressing
`ctrl-c` will halt the running containers.

### Stopping

```
docker-compose down
```

### Clearing Containers

```
docker-compose rm
```

Remember that the loaded data is stored in the `data` folder. This can be more
than 100GB if you load all of wikipedia. Files in it may be owned by root.

### Problems

The Elastic Search docker compose configuration may fail to start with the
following error:

```
max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

If that happens then update the system configuration with the following command
(linux):

```bash
sysctl -w vm.max_map_count=262144
```

You can find more details in the
[Elastic Search docker compose documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html).

Data Loader
-----------

This is a java application that can load the Wikipedia XML into Elastic Search
and Postgres. This requires Java 8.

### Usage

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
