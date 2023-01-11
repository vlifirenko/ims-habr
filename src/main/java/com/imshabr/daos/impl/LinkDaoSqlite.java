package com.imshabr.daos.impl;

import com.imshabr.daos.LinkDao;
import com.imshabr.vos.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class LinkDaoSqlite implements LinkDao {
    private final Logger LOGGER = LoggerFactory.getLogger(LinkDaoSqlite.class);

    private static final String TABLE = "links";
    private static final String FIELD_ID = "id";
    private static final String FIELD_ID1 = "id1";
    private static final String FIELD_ID2 = "id2";

    private Connection c = null;

    public LinkDaoSqlite() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:imshabr.db");
            c.setAutoCommit(false);
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Opened database successfully");
        createTable();
    }

    private void createTable() {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE +
                    "(" + FIELD_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_ID1 + " INT, " +
                    FIELD_ID2 + " INT) ";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Table created successfully");
    }

    @Override
    public long saveLink(Link link) {
        Statement stmt = null;
        long id = -1;
        try {
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO %s (%s,%s) " +
                            "VALUES (%s,%s);",
                    TABLE, FIELD_ID1, FIELD_ID2,
                    link.nodeId1, link.nodeId2);
            stmt.executeUpdate(sql);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (stmt.getGeneratedKeys().next()) {
                id = generatedKeys.getLong(1);
            } else {
                return -1;
            }
            stmt.close();
            c.commit();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Records created successfully");
        return id;
    }

    @Override
    public void updateLink(Link link) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = String.format("UPDATE %s set %s = %s, %s = %s where %s = %d%n",
                    TABLE, FIELD_ID1, link.nodeId1, FIELD_ID2, link.nodeId2,
                    FIELD_ID, link.getId());
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Update successfully");
    }

    @Override
    public Link getById(long id) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s WHERE %s = %d%n", TABLE, FIELD_ID, id));
            if (!rs.next()) {
                return null;
            }
            Link link = new Link(rs.getInt(FIELD_ID), rs.getInt(FIELD_ID1), rs.getInt(FIELD_ID2));
            rs.close();
            stmt.close();
            LOGGER.info("getById success");
            LOGGER.info(link.toString());
            return link;
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteLink(Link link) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = String.format("DELETE from %s where %s = %d%n", TABLE, FIELD_ID, link.getId());
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("deleteNode successfully");
    }

    @Override
    public void close() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
