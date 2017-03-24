# Elastic Search

Uses Inverted Index

Calculates score

Ranking as well as Filtering


---
ALGORITHMS-ELASTICSEARCH

Elasticsearch uses a structure called an inverted index, which is designed to allow very fast full-text searches. An inverted index consists of a list of all the unique words that appear in any document, and for each word, a list of the documents in which it appears.

Elasticsearch uses Apache Lucene to create and manage this inverted index.
https://www.elastic.co/guide/en/elasticsearch/guide/current/inverted-index.html
http://www.elasticsearchtutorial.com/basic-elasticsearch-concepts.html

Analysis is the process of converting text, like the body of any email, into tokens or terms which are added to the inverted index for searching. Analysis is performed by an analyzer which can be either a built-in analyzer or a custom analyzer defined per index.
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html

Elasticsearch ships with a wide range of built-in analyzers, which can be used in any index without further configuration:
https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-analyzers.html

