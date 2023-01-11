package com.imshabr.daos;

import com.imshabr.vos.Link;
import com.imshabr.vos.Node;

public interface LinkDao {

    public abstract long saveLink(Link link);

    public abstract void updateLink(Link link);

    public abstract Link getById(long id);

    public abstract void deleteLink(Link link);

    public abstract void close();
}
