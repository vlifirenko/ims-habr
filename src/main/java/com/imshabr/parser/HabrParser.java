package com.imshabr.parser;

import com.imshabr.daos.LinkDao;
import com.imshabr.daos.NodeDao;
import com.imshabr.daos.impl.LinkDaoSqlite;
import com.imshabr.daos.impl.NodeDaoSqlite;
import com.imshabr.parser.vos.FeedItem;
import com.imshabr.vos.Link;
import com.imshabr.vos.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HabrParser {
    private final Logger LOGGER = LoggerFactory.getLogger(HabrParser.class);

    static final String ITEM = "item";
    static final String TITLE = "title";
    static final String LINK = "link";
    static final String DESCRIPTION = "description";
    static final String GUID = "guid";
    static final String PUB_DATE = "pubDate";
    static final String AUTHOR = "author";
    static final String CATEGORY = "category";

    private URL url;

    public HabrParser(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void readFeed() {
        try {
            String title = "";
            String link = "";
            String description = "";
            String pubDate = "";
            String guid = "";
            String author = "";
            List<String> categories = new ArrayList<String>();

            NodeDao nodeDao = new NodeDaoSqlite();
            List<Link> links = new ArrayList<Link>();

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    if (localPart.equals(ITEM)) {
                        eventReader.nextEvent();
                    } else if (localPart.equals(TITLE)) {
                        title = getCharacterData(eventReader);
                    } else if (localPart.equals(LINK)) {
                        link = getCharacterData(eventReader);
                    } else if (localPart.equals(DESCRIPTION)) {
                        description = getCharacterData(eventReader);
                    } else if (localPart.equals(PUB_DATE)) {
                        pubDate = getCharacterData(eventReader);
                    } else if (localPart.equals(GUID)) {
                        guid = getCharacterData(eventReader);
                    } else if (localPart.equals(AUTHOR)) {
                        author = getCharacterData(eventReader);
                    } else if (localPart.equals(CATEGORY)) {
                        categories.add(getCharacterData(eventReader));
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                        FeedItem feedItem = new FeedItem(title, guid, link, description, pubDate, author);
                        feedItem.categories = categories;
                        Node node = new Node("page", feedItem.guid, feedItem.title, feedItem.description, "");
                        node.setId(nodeDao.saveNode(node));

                        for (String category : feedItem.categories) {
                            Node nodeTag = nodeDao.getByTitle(category);
                            if (nodeTag == null) {
                                nodeTag = new Node("tag", "", category, "", generateColor());
                                nodeTag.setId(nodeDao.saveNode(nodeTag));
                            }
                            links.add(new Link(node.getId(), nodeTag.getId()));
                        }
                        eventReader.nextEvent();
                    }
                }
            }
            nodeDao.close();
            LinkDao linkDao = new LinkDaoSqlite();
            for (Link linkNode : links) {
                linkDao.saveLink(linkNode);
            }
            linkDao.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCharacterData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateColor() {
        Random rand = new Random();
        float h = rand.nextFloat();
        float s = 1;
        float l = 0.65f;
        Map<String, Integer> rgb = hslToRgb(h, s, l);
        return String.format("rgb(%s, %s, %s)", rgb.get("r"), rgb.get("g"), rgb.get("b"));
    }

    private Map<String, Integer> hslToRgb(float h, float s, float l) {
        float r, g, b;
        if (s == 0) {
            r = g = b = (int) l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hue2rgb(p, q, h + 1f / 3f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1f / 3f);
        }
        Map<String, Integer> rgb = new HashMap<String, Integer>();
        rgb.put("r", Math.round(r * 255f));
        rgb.put("g", Math.round(g * 255f));
        rgb.put("b", Math.round(b * 255f));
        return rgb;
    }

    private float hue2rgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }
}
