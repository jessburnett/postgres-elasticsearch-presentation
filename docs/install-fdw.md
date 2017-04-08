Install Elastic Search FDW
==========================

This installs the foreign data wrapper into postgres and creates the remote table.

Install FDW Package
-------------------

You have to enter the Postgres Docker image to install the FDW package. The
following commands will install the package provided that you are running the
Postgres container that is defined by the docker-compose.yml file:

```
docker exec -ti postgres bash <<<'
  apt-get update ;
  apt-get install --yes postgresql-9.6-python-multicorn python python-pip git ;
  pip install elasticsearch ;

  git clone https://github.com/matthewfranglen/postgres-elasticsearch-fdw postgres-elasticsearch-fdw
  cd postgres-elasticsearch-fdw
  python setup.py install
'
```

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

Then create the remote table:


```
echo "CREATE FOREIGN TABLE articles_es
    (
        id TEXT,
        title TEXT,
        body TEXT
    )
SERVER multicorn_es
OPTIONS
    (
        host 'elasticsearch',
        port '9200',
        index 'wikipedia',
        type 'elasticsearchdocument'
    )
;" | psql -p 5432 -h localhost -U postgres postgres
```
