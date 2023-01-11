package com.imshabr.parser.vos;

import java.util.ArrayList;
import java.util.List;

public class FeedItem {

    public String title;
    public String guid;
    public String link;
    public String description;
    public String pubDate;
    public String author;
    public List<String> categories = new ArrayList<String>();

    public FeedItem(String title, String guid, String link, String description, String pubDate, String author) {
        this.title = title;
        this.guid = guid;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format("title:%s,guid:%s,link:%s,description:%s,pubDate:%s,author:%s",
                title, guid, link, description, pubDate, author);
    }
}
