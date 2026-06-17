package com.student.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class that centralises JDBC connection management.
 *
 * Configuration is read from system properties so the same WAR can be
 * deployed to different environments without recompiling:
 *
 *   -Ddb.url=jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC
 *   -Ddb.user=root
 *   -Ddb.password=secret
 *
 * If the properties are absent the hard-coded defaults below are used.
 */
public class DBConnection {

    // ── Default connection settings (override via system properties) ──────────
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/student_db" +
            "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DEFAULT_USER     = "root";
    private static final String DEFAULT_PASSWORD = "12345";

    static {
        try {
            // Explicitly load the driver — required for some containers.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC driver not found on classpath: " + e.getMessage());
        }
    }

    /** Returns a new {@link Connection} using the configured credentials. */
    public static Connection getConnection() throws SQLException {
        String url  = System.getProperty("db.url",      DEFAULT_URL);
        String user = System.getProperty("db.user",     DEFAULT_USER);
        String pass = System.getProperty("db.password", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, pass);
    }

    /** Closes a connection silently (null-safe). */
    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    // Utility class — no instances needed.
    private DBConnection() {}
}
