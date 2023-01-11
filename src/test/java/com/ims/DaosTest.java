package com.ims;

import com.imshabr.daos.LinkDao;
import com.imshabr.daos.NodeDao;
import com.imshabr.daos.impl.LinkDaoSqlite;
import com.imshabr.daos.impl.NodeDaoSqlite;
import com.imshabr.vos.Link;
import com.imshabr.vos.Node;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaosTest {
    private final Logger LOGGER = LoggerFactory.getLogger(DaosTest.class);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void nodeDaoTest() throws Exception {
        NodeDao nodeDao = new NodeDaoSqlite();
        Node node = new Node("type", "url", "title", "description", "rgb(100,100,100");
        // insert & select
        long id = nodeDao.saveNode(node);
        LOGGER.info("id:" + id);
        Assert.assertNotEquals("INSERT: no return id", id, -1);
        Node returnNode = nodeDao.getById(id);
        node.setId(returnNode.getId());
        Assert.assertNotNull("INSERT&SELECT: return node is null", returnNode);
        Assert.assertEquals("INSERT&SELECT: node isn't equals", node, returnNode);
        // update
        node = new Node(id, "newType", "newUrl", "newTitle", "newDescription", "rgb(200,200,200");
        nodeDao.updateNode(node);
        returnNode = nodeDao.getById(node.getId());
        Assert.assertNotNull("UPDATE: return node is null", returnNode);
        Assert.assertEquals("UPDATE:node isn't equals", node, returnNode);
        // delete
        nodeDao.deleteNode(node);
        returnNode = nodeDao.getById(node.getId());
        Assert.assertNull("DELETE: return node isn't null", returnNode);
    }

    @Test
    public void linkDaoTest() throws Exception {
        LinkDao linkDao = new LinkDaoSqlite();
        Link link = new Link(10, 20);
        // insert & select
        long id = linkDao.saveLink(link);
        LOGGER.info("id:" + id);
        Assert.assertNotEquals("INSERT: no return id", id, -1);
        Link returnLink = linkDao.getById(id);
        link.setId(returnLink.getId());
        Assert.assertNotNull("INSERT&SELECT: return node is null", returnLink);
        Assert.assertEquals("INSERT&SELECT: node isn't equals", link, returnLink);
        // update
        link = new Link(id, 15, 25);
        linkDao.updateLink(link);
        returnLink = linkDao.getById(link.getId());
        Assert.assertNotNull("UPDATE: return node is null", returnLink);
        Assert.assertEquals("UPDATE:node isn't equals", link, returnLink);
        // delete
        linkDao.deleteLink(link);
        returnLink = linkDao.getById(link.getId());
        Assert.assertNull("DELETE: return node isn't null", returnLink);
    }

}
