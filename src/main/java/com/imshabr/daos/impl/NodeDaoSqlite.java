package com.imshabr.daos.impl;

import com.imshabr.daos.NodeDao;
import com.imshabr.vos.Node;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class NodeDaoSqlite implements NodeDao {
    private final Logger LOGGER = LoggerFactory.getLogger(NodeDaoSqlite.class);

    private static final String TABLE = "nodes";
    private static final String FIELD_ID = "id";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_URL = "webdav_url";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_COLOR = "color";

    private Connection connection = null;

    public NodeDaoSqlite() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:imshabr.db");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Opened database successfully");
        createTable();
    }

    private void createTable() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE +
                    "(" + FIELD_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    FIELD_TYPE + " VARCHAR(100)  NOT NULL, " +
                    FIELD_URL + " VARCHAR(100)   NOT NULL, " +
                    FIELD_NAME + " VARCHAR(100) NOT NULL, " +
                    FIELD_DESCRIPTION + " TEXT   NOT NULL, " +
                    FIELD_COLOR + " VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Table created successfully");
    }

    @Override
    public long saveNode(Node node) {
        Statement stmt = null;
        long id = -1;
        String sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s');",
                TABLE, FIELD_TYPE, FIELD_URL, FIELD_NAME, FIELD_DESCRIPTION, FIELD_COLOR,
                node.type, node.url, node.title, StringEscapeUtils.escapeSql(node.description), node.color);
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (stmt.getGeneratedKeys().next()) {
                id = generatedKeys.getLong(1);
            } else {
                return -1;
            }
            stmt.close();
            connection.commit();
        } catch (Exception e) {
            LOGGER.error(sql);
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Records created successfully");
        return id;
    }

    @Override
    public void updateNode(Node node) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = String.format("UPDATE %s set %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' where %s = %d%n",
                    TABLE, FIELD_TYPE, node.type, FIELD_URL, node.url, FIELD_NAME, node.title, FIELD_DESCRIPTION, node.description, FIELD_COLOR, node.color,
                    FIELD_ID, node.getId());
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        LOGGER.info("Update successfully");
    }

    @Override
    public Node getById(long id) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = String.format("SELECT * FROM %s WHERE %s = %s", TABLE, FIELD_ID, id);
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            Node node = new Node(rs.getInt(FIELD_ID), rs.getString(FIELD_TYPE), rs.getString(FIELD_URL),
                    rs.getString(FIELD_NAME), rs.getString(FIELD_DESCRIPTION), rs.getString(FIELD_COLOR));
            rs.close();
            stmt.close();
            LOGGER.info("getById success");
            LOGGER.info(node.toString());
            return node;
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public Node getByTitle(String title) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE, FIELD_NAME, title);
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            Node node = new Node(rs.getInt(FIELD_ID), rs.getString(FIELD_TYPE), rs.getString(FIELD_URL),
                    rs.getString(FIELD_NAME), rs.getString(FIELD_DESCRIPTION), rs.getString(FIELD_COLOR));
            rs.close();
            stmt.close();
            LOGGER.info("getById success");
            return node;
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteNode(Node node) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = String.format("DELETE from %s where %s = %d%n", TABLE, FIELD_ID, node.getId());
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("deleteNode successfully");
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
