Indexing Postgres
=================

The Postgres data must be indexed. Elastic Search indexes the documents as they
are loaded.

The index only works over a `tsvector` data type. Unfortunately this data type
has a fixed maximum size, and the current wikipedia data dump has entries which
exceed that. This means that it is not possible to index over a function on the
body of all wikipedia pages, as some of them will fail.

So an additional column must be added, and the body must be reliably converted.

Add Column
----------

To add the column just run:

```
ALTER TABLE postgres_document ADD COLUMN body_tsvector tsvector;
```

This can be run from outside the docker container with this command:

```
echo "ALTER TABLE postgres_document ADD COLUMN body_tsvector tsvector;" | psql -p 5432 -h localhost -U postgres postgres
```

Load Column
-----------

The `bin/index-column.sh` script can load the `body_tsvector` column from the
`body` column reliably. It works by copying over in blocks and then repeating
those blocks that fail with a smaller block size until it copies individual
blocks. At this point all the failing blocks are the problem rows.

This script relies on the column names. To run it:

```
bin/index-column.sh
```

Index Column
------------

The column can then be indexed using GIN (inverted index) or GiST (bloom filter) indexes.

If you have loaded the entire wikipedia dump then this can take a very long time.

### GIN Index

The GIN Index takes the longest to create. It is the faster of the two indexes when used.

```
echo "CREATE INDEX idx_gin ON postgres_document USING gin (body_tsvector);" | psql -p 5432 -h localhost -U postgres postgres
```

### GiST Index

The GiST index is faster to create and update. It is slower than the GIN index
because a bloom filter can have false positives. This means that each matching
row must be checked again. Postgres does this, however it takes time.

```
echo "CREATE INDEX idx_gist ON postgres_document USING gist (body_tsvector);" | psql -p 5432 -h localhost -U postgres postgres
```

Next Steps
----------

Now you can test the two indexes. See the instructions [here](test.md)
