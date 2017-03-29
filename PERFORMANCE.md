# Postgres

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

    postgres=# select id, octet_length(body) from postgres_document where body_tsvector is null and octet_length(body) <= 1048575;
        id    │ octet_length
    ──────────┼──────────────
     38456420 │       963975
     38675580 │       856815
     43630307 │      1027898
     50127418 │       832042
    (4 rows)

    postgres=# create index idx_gin on postgres_document_gin using gin (body_tsvector);
    CREATE INDEX
    Time: 228704875.253 ms

    postgres=# create index idx_gist on postgres_document_gist using gist (body_tsvector);
    CREATE INDEX
    Time: 8286613.247 ms

    0.09591


    postgres=# select title, ts_rank(body_tsvector, query) as rank from postgres_document_gin, to_tsquery('england') query where query @@ body_tsvector order by rank desc limit 10;
    Time: 971726.661 ms

    postgres=# select title, ts_rank(body_tsvector, query) as rank from postgres_document_gin, to_tsquery('england') query where query @@ body_tsvector order by rank desc limit 10;
                          title                       │   rank
    ──────────────────────────────────────────────────┼───────────
     Wikipedia:WikiProject Football/Members           │ 0.0997621
     Andrew Strauss                                   │ 0.0997621
     List of churches in London                       │ 0.0997621
     History of the England national rugby union team │ 0.0997621
     Grade II* listed buildings in South Somerset     │ 0.0997621
     List of Sheffield Wednesday F.C. players         │ 0.0997621
     Scheduled monuments in Mendip                    │ 0.0997621
     Eoin Morgan                                      │ 0.0997621
     List of cricket commentators                     │ 0.0997621
     England national rugby union team                │ 0.0997621
    (10 rows)

    Time: 945716.201 ms

    postgres=# select title, ts_rank(body_tsvector, query) as rank, char_length(body) as body from postgres_document_gin, to_tsquery('england') query where query @@ body_tsvector order by rank desc limit 10;
                          title                       │   rank    │  body
    ──────────────────────────────────────────────────┼───────────┼────────
     Wikipedia:WikiProject Football/Members           │ 0.0997621 │ 179688
     Andrew Strauss                                   │ 0.0997621 │ 106882
     List of churches in London                       │ 0.0997621 │ 245394
     History of the England national rugby union team │ 0.0997621 │  98377
     Grade II* listed buildings in South Somerset     │ 0.0997621 │ 167373
     List of Sheffield Wednesday F.C. players         │ 0.0997621 │  62494
     Scheduled monuments in Mendip                    │ 0.0997621 │ 261449
     Eoin Morgan                                      │ 0.0997621 │  67160
     List of cricket commentators                     │ 0.0997621 │  40397
     England national rugby union team                │ 0.0997621 │  85524
    (10 rows)

    Time: 957441.885 ms

# Elastic Search

    ➜ time curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H 'Content-Type: application/json' -d'
    {
      "query": { "match": { "body": "england" } }
    }
    '
    {
      "took" : 3568,
      "timed_out" : false,
      "_shards" : {
        "total" : 5,
        "successful" : 5,
        "failed" : 0
      },
      "hits" : {
        "total" : 559739,
        "max_score" : 3.1234846,
        "hits" : [ {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "4295683",
          "_score" : 3.1234846,
          "_source" : {
            "id" : "4295683",
            "title" : "England, your england",
            "body" : "#REDIRECT [[England Your England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "5541146",
          "_score" : 3.122929,
          "_source" : {
            "id" : "5541146",
            "title" : "England, Your England",
            "body" : "#REDIRECT [[England Your England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "4940535",
          "_score" : 2.7607965,
          "_source" : {
            "id" : "4940535",
            "title" : "UK, (England)",
            "body" : "#REDIRECT [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "6918433",
          "_score" : 2.7607965,
          "_source" : {
            "id" : "6918433",
            "title" : "English Nation",
            "body" : "#REDIRECT [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "2592711",
          "_score" : 2.7607965,
          "_source" : {
            "id" : "2592711",
            "title" : "Anglica",
            "body" : "#redirect [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "32932397",
          "_score" : 2.7607965,
          "_source" : {
            "id" : "32932397",
            "title" : "Aenglaland",
            "body" : "#REDIRECT [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "19895730",
          "_score" : 2.7603054,
          "_source" : {
            "id" : "19895730",
            "title" : "Category:Transportation in New England",
            "body" : "[[Category:New England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "31739155",
          "_score" : 2.7603054,
          "_source" : {
            "id" : "31739155",
            "title" : "England, United Kingdom",
            "body" : "#REDIRECT [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "32932409",
          "_score" : 2.7603054,
          "_source" : {
            "id" : "32932409",
            "title" : "Aengland",
            "body" : "#REDIRECT [[England]]"
          }
        }, {
          "_index" : "wikipedia",
          "_type" : "elasticsearchdocument",
          "_id" : "26914642",
          "_score" : 2.757912,
          "_source" : {
            "id" : "26914642",
            "title" : "Land of the Angles",
            "body" : "#REDIRECT [[England]]"
          }
        } ]
      }
    }
    curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H    0.00s user 0.00s system 0% cpu 3.585 total

    ➜ time curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H 'Content-Type: application/json' -d'
    {
      "query": { "match": { "body": "england" } }
    }
    ' | jq '[ .hits.hits[] | { title: ._source.title, rank: ._score, body: ._source.body | length } ]'
    [
      {
        "title": "England, your england",
        "rank": 3.1234846,
        "body": 34
      },
      {
        "title": "England, Your England",
        "rank": 3.122929,
        "body": 34
      },
      {
        "title": "UK, (England)",
        "rank": 2.7607965,
        "body": 21
      },
      {
        "title": "English Nation",
        "rank": 2.7607965,
        "body": 21
      },
      {
        "title": "Anglica",
        "rank": 2.7607965,
        "body": 21
      },
      {
        "title": "Aenglaland",
        "rank": 2.7607965,
        "body": 21
      },
      {
        "title": "Category:Transportation in New England",
        "rank": 2.7603054,
        "body": 24
      },
      {
        "title": "England, United Kingdom",
        "rank": 2.7603054,
        "body": 21
      },
      {
        "title": "Aengland",
        "rank": 2.7603054,
        "body": 21
      },
      {
        "title": "Land of the Angles",
        "rank": 2.757912,
        "body": 21
      }
    ]
    curl -XGET 'localhost:9200/wikipedia/_search?pretty' -H    0.00s user 0.00s system 0% cpu 0.017 total
    jq   0.00s user 0.00s system 0% cpu 0.017 total
