COMPLEXITY-POSTGRES

echo "select count(*) from postgres_document where body ilike '%england%';" | psql -h localhost -U postgres postgres
 count
────────
 567601
(1 row)

Time: 827393.690 ms

➜ echo "select count(*) from postgres_document where body like '%England%';" | psql -h localhost -U postgres postgres
 count
────────
 559122
(1 row)

Time: 490876.806 ms

➜ echo "select count(*) from postgres_document where body ~* '.*England.*';" | psql -h localhost -U postgres postgres
 count
────────
 567601
(1 row)

Time: 586185.781 ms

➜ echo "select count(*) from postgres_document where body ~ '.*England.*';" | psql -h localhost -U postgres postgres
 count
────────
 559122
(1 row)

Time: 555776.412 ms

➜ time curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": { "match": { "body": "england" } }
}
' | jq .hits.total
559739
curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H    0.00s user 0.00s system 0% cpu 1.596 total
jq .hits.total  0.00s user 0.00s system 0% cpu 1.596 total

postgres=# alter table postgres_document add column body_tsvector tsvector;
ALTER TABLE
Time: 26.486 ms

update postgres_document set body_tsvector = to_tsvector('english', body);
update postgres_document set body_tsvector = to_tsvector('english', substring(body from 0 to 1000000));
update postgres_document set body_tsvector = to_tsvector('english', body) where octet_length(body) <= 1048575;

ERROR:  string is too long for tsvector (1216758 bytes, max 1048575 bytes)
Time: 11999814.588 ms

postgres=# insert into document_lengths (id, length) select postgres_document.id, octet_length(postgres_document.body) from postgres_document;
INSERT 0 17008269
Time: 724027.175 ms

postgres=# create index idx_document_length on document_lengths (length);
CREATE INDEX
Time: 16060.637 ms

postgres=# select count(length) from document_lengths where length > 1048575;
 count 
───────
    96
(1 row)

Time: 33.200 ms

horrific approach:
time (
    echo "select id from postgres_document;" | psql -q -t -X -h localhost -U postgres postgres | sed -e '/^$/d' | while read id
    do
        if ! echo "update postgres_document set body_tsvector = to_tsvector('english', body) where id = '${id}';" | psql -h localhost -U postgres postgres >/dev/null 2>&1
        then
            echo "Unable to update ${id}"
        fi
    done
)
