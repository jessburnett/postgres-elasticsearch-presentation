```
CREATE FOREIGN TABLE elasticsearch_document
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
;
```
