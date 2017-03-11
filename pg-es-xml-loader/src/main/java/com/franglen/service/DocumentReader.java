package com.franglen.service;

import static com.google.common.base.Preconditions.checkState;

import java.io.CharArrayWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.franglen.dto.WikipediaDocument;

@Service
public class DocumentReader {

    private final DocumentGateway gateway;

    public DocumentReader(DocumentGateway gateway) {
        this.gateway = gateway;
    }

    public void read(String document) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        // spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(new WikipediaContentHandler());
        xmlReader.parse(document);
    }

    private class WikipediaContentHandler extends DefaultHandler {

        private WikipediaDocument.Builder document;
        private HandlerState state;
        private final CharArrayWriter buffer;

        public WikipediaContentHandler() {
            state = HandlerState.OUTSIDE_PAGE;
            buffer = new CharArrayWriter();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            switch (qName) {
                case "page":
                    startPage();
                    return;
                case "id":
                    startId();
                    return;
                case "title":
                    startTitle();
                    return;
                case "text":
                    startBody();
                    return;
                case "revision":
                    startRevision();
                    return;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            switch (qName) {
                case "page":
                    endPage();
                    return;
                case "id":
                    endId();
                    return;
                case "title":
                    endTitle();
                    return;
                case "text":
                    endBody();
                    return;
                case "revision":
                    endRevision();
                    return;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            switch(state) {
                case ID:
                case TITLE:
                case BODY:
                    buffer.write(ch, start, length);
                    break;
                default:
            }
        }

        private void startPage() {
            checkState(state == HandlerState.OUTSIDE_PAGE);

            state = HandlerState.PAGE;
            document = WikipediaDocument.builder();
        }

        private void endPage() {
            checkState(state == HandlerState.PAGE);

            state = HandlerState.OUTSIDE_PAGE;
            gateway.accept(document.build());
        }

        private void startId() {
            if (state == HandlerState.REVISION) {
                return;
            }
            checkState(state == HandlerState.PAGE);

            state = HandlerState.ID;
            buffer.reset();
        }

        private void endId() {
            if (state == HandlerState.REVISION) {
                return;
            }
            checkState(state == HandlerState.ID);

            state = HandlerState.PAGE;
            document.id(buffer.toString());
        }

        private void startTitle() {
            if (state == HandlerState.REVISION) {
                return;
            }
            checkState(state == HandlerState.PAGE);

            state = HandlerState.TITLE;
            buffer.reset();
        }

        private void endTitle() {
            if (state == HandlerState.REVISION) {
                return;
            }
            checkState(state == HandlerState.TITLE);

            state = HandlerState.PAGE;
            document.title(buffer.toString());
        }

        private void startBody() {
            checkState(state == HandlerState.REVISION);

            state = HandlerState.BODY;
            buffer.reset();
        }

        private void endBody() {
            checkState(state == HandlerState.BODY);

            state = HandlerState.REVISION;
            document.body(buffer.toString());
        }

        private void startRevision() {
            checkState(state == HandlerState.PAGE);

            state = HandlerState.REVISION;
        }

        private void endRevision() {
            checkState(state == HandlerState.REVISION);

            state = HandlerState.PAGE;
        }

    }

    private enum HandlerState {
        OUTSIDE_PAGE,
        PAGE,
        ID,
        TITLE,
        BODY,
        REVISION;
    }

}
