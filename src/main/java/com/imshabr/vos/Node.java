package com.imshabr.vos;

public class Node {

    private long id;
    public String type;
    public String url;
    public String title;
    public String description;
    public String color;

    public Node(long id, String type, String url, String title, String description, String color) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public Node(String type, String url, String title, String description, String color) {
        this.type = type;
        this.url = url;
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("id:%s,type:%s,url:%s,title:%s,description:%s",
                id, type, url, title, description);
    }

    @Override
    public boolean equals(Object obj) {
        Node node = (Node) obj;
        return (node.getId() == id && node.type.equals(type) &&
                node.url.equals(url) && node.description.equals(description));
    }
}
