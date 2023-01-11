package com.imshabr.daos;

import com.imshabr.vos.Node;

public interface NodeDao {

    public abstract long saveNode(Node node);

    public abstract void updateNode(Node node);

    public abstract Node getById(long id);

    public abstract Node getByTitle(String title);

    public abstract void deleteNode(Node node);

    public abstract void close();
}
