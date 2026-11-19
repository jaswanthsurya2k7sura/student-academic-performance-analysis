package com.vcet.studentanalysis.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Database Connection Manager using JDBC.
 * Automatically resolves database path.
 */
public class DBConnection {
    private static Connection connection = null;

    private DBConnection() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load SQLite JDBC Driver class
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found in classpath: " + e.getMessage());
            }

            // Check possible database file paths
            String[] candidatePaths = {
                "d:/project/database/student_performance.db",
                "database/student_performance.db",
                "../database/student_performance.db",
                System.getProperty("user.dir") + File.separator + "database" + File.separator + "student_performance.db"
            };

            String resolvedPath = null;
            for (String path : candidatePaths) {
                File file = new File(path);
                if (file.exists()) {
                    resolvedPath = file.getAbsolutePath();
                    break;
                }
            }

            if (resolvedPath == null) {
                // Default to standard workspace path
                resolvedPath = "d:/project/database/student_performance.db";
            }

            String url = "jdbc:sqlite:" + resolvedPath.replace("\\", "/");
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
        }
    }
}
