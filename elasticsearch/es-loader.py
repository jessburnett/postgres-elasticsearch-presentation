# This loads the Wikipedia XML data into elasticsearch

import argparse
import itertools
from xml.etree import ElementTree

def main():
    parser = argparse.ArgumentParser(description='Load Wikipedia XML into Elastic Search.')
    parser.add_argument('filenames', metavar='FILE', type=str, nargs='+', help='the files to load')
    args = parser.parse_args()

    for documents in grouper(10, XmlConsumer(args.filenames)):
        print(list)
        return

class XmlConsumer:
    def __init__(self, filenames):
        self._iterable = node_iterator(filenames)

    def is_page(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}page"

    def is_title(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}title"

    def is_text(self, element):
        return element.tag == "{http://www.mediawiki.org/xml/export-0.10/}text"

    def make_document(self, title, text):
        return {
            "title": title,
            "body": text
        }

    def __iter__(self):
        return self

    def __next__(self):
        for element in self._iterable:
            if self.is_title(element):
                self._title = element.text
            elif self.is_text(element):
                return self.make_document(self._title, element.text)

        raise StopIteration

    def next(self):
        return self.__next__()

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
        yield list(chunk)

if __name__ == "__main__":
    main()
