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

postgres=# vacuum full;
VACUUM
Time: 15071501.037 ms

postgres=# create table postgres_document_gin (id varchar(255) primary key, body text, title text, body_tsvector tsvector);
CREATE TABLE
Time: 55.647 ms
postgres=# create table postgres_document_gist (id varchar(255) primary key, body text, title text, body_tsvector tsvector);
CREATE TABLE
Time: 12.023 ms

postgres=# insert into postgres_document_gin select * from postgres_document;
INSERT 0 17008269
Time: 8991312.925 ms

insert into postgres_document_gist select * from postgres_document;
INSERT 0 17008269
Time: 8721773.457 ms

create index idx_gin on postgres_document_gin using gin (body_tsvector);
^CCancel request sent
^CCancel request sent
ERROR:  canceling statement due to user request
Time: 147382257.256 ms

^CCancel request sent
ERROR:  canceling statement due to user request
Time: 2817.111 ms
create index idx_gist on postgres_document_gist using gist (body_tsvector);

postgres=# show maintenance_work_mem;
 maintenance_work_mem
──────────────────────
 64MB
(1 row)

Time: 1.135 ms

postgres=# set maintenance_work_mem = '1GB';
SET
Time: 0.259 ms
postgres=# show maintenance_work_mem;
 maintenance_work_mem
──────────────────────
 1GB
(1 row)

Time: 0.216 ms

postgres=# create index idx_gin on postgres_document_gin using gin (body_tsvector);
...
