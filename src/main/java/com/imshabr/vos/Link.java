package com.imshabr.vos;

public class Link {

    private long id;
    public long nodeId1;
    public long nodeId2;

    public Link(long id, long nodeId1, long nodeId2) {
        this.id = id;
        this.nodeId1 = nodeId1;
        this.nodeId2 = nodeId2;
    }

    public Link(long nodeId1, long nodeId2) {
        this.nodeId1 = nodeId1;
        this.nodeId2 = nodeId2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("id:%s,nodeId1:%s,nodeId2:%s", id, nodeId1, nodeId2);
    }

    @Override
    public boolean equals(Object obj) {
        Link link = (Link) obj;
        return (link.getId() == id && link.nodeId1 == nodeId1 && link.nodeId2 == nodeId2);
    }
}
