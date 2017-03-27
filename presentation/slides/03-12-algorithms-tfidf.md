ALGORITHMS-TFIDF

Lucene (and thus Elasticsearch) uses the Boolean model to find matching documents, and a formula called the practical scoring function to calculate relevance. This formula borrows concepts from term frequency/inverse document frequency and the vector space model but adds more-modern features like a coordination factor, field length normalization, and term or query clause boosting.
https://www.elastic.co/guide/en/elasticsearch/guide/current/sorting.html
https://www.elastic.co/guide/en/elasticsearch/guide/current/scoring-theory.html
https://www.elastic.co/guide/en/elasticsearch/guide/current/relevance-intro.html

Enter the constant_score query. This query can wrap either a query or a filter, and assigns a score of 1 to any documents that match, regardless of TF/IDF:
https://www.elastic.co/guide/en/elasticsearch/guide/current/ignoring-tfidf.html
