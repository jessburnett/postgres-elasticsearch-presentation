COMPLEXITY-ELASTICSEARCH

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

