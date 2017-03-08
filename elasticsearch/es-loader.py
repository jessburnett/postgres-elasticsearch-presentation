# This loads the Wikipedia XML data into elasticsearch

import argparse
import itertools
from xml.etree import ElementTree
from elasticsearch_dsl import DocType, Text
from elasticsearch_dsl.connections import connections
from elasticsearch.helpers import bulk

CHUNK_SIZE=250 * 1000

def main():
    parser = argparse.ArgumentParser(description='Load Wikipedia XML into Elastic Search.')
    parser.add_argument('filenames', metavar='FILE', type=str, nargs='+', help='the files to load')
    args = parser.parse_args()

    connections.create_connection(hosts=['localhost'])
    ElasticDocument.init()

    for chunk in grouper(CHUNK_SIZE, XmlConsumer(filenames)):
        bulk(connections.get_connection(), chunk)

def node_iterator(filenames):
    for filename in filenames:
        for event, element in ElementTree.iterparse(filename):
            yield element

def grouper(n, iterable):
    it = iter(iterable)
    while True:
        chunk = itertools.islice(it, n)
        if not chunk:
            return
        yield chunk

class XmlConsumer:
    def __init__(self, filenames):
        self._iterable = node_iterator(filenames)

    def is_id(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}id"

    def is_title(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}title"

    def is_text(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}text"

    def make_document(self, id, title, text):
        return ElasticDocument(meta={"id": id}, title=title, body=text).to_dict(True)

    def __iter__(self):
        id = None
        title = None

        for element in self._iterable:
            if self.is_id(element):
                id = element.text
            if self.is_title(element):
                title = element.text
            elif self.is_text(element):
                yield self.make_document(id, title, element.text)
            element.clear()

class ElasticDocument(DocType):
    title = Text()
    body = Text()

    class Meta:
        index = 'wikipedia'

if __name__ == "__main__":
    main()
