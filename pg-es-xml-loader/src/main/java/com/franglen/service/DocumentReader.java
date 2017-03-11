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
                    checkState(state == HandlerState.OUTSIDE_PAGE);
                    state = HandlerState.PAGE;
                    document = WikipediaDocument.builder();
                    return;
                case "id":
                    if (state == HandlerState.OTHER) {
                        return;
                    }
                    checkState(state == HandlerState.PAGE);
                    state = HandlerState.ID;
                    buffer.reset();
                    return;
                case "title":
                    if (state == HandlerState.OTHER) {
                        return;
                    }
                    checkState(state == HandlerState.PAGE);
                    state = HandlerState.TITLE;
                    buffer.reset();
                    return;
                case "text":
                    if (state == HandlerState.OTHER) {
                        return;
                    }
                    checkState(state == HandlerState.PAGE);
                    state = HandlerState.BODY;
                    buffer.reset();
                    return;
                case "revision":
                    checkState(state == HandlerState.PAGE);
                    state = HandlerState.OTHER;
                    return;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            switch (qName) {
                case "page":
                    checkState(state == HandlerState.PAGE);
                    state = HandlerState.OUTSIDE_PAGE;
                    gateway.accept(document.build());
                    return;
                case "id":
                    if (state != HandlerState.ID) {
                        return;
                    }
                    state = HandlerState.PAGE;
                    document.id(buffer.toString());
                    return;
                case "title":
                    if (state != HandlerState.TITLE) {
                        return;
                    }
                    state = HandlerState.PAGE;
                    document.title(buffer.toString());
                    return;
                case "text":
                    if (state != HandlerState.BODY) {
                        return;
                    }
                    state = HandlerState.PAGE;
                    document.body(buffer.toString());
                    return;
                case "revision":
                    checkState(state == HandlerState.OTHER);
                    state = HandlerState.PAGE;
                    return;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            buffer.write(ch, start, length);
        }

    }

    private enum HandlerState {
        OUTSIDE_PAGE,
        PAGE,
        ID,
        TITLE,
        BODY,
        OTHER;
    }

}
