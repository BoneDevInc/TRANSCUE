package com.bonesnetwork.TRANSQUE.util;

import com.bonesnetwork.TRANSQUE.TRANSQUE;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDB {
    private static Connection connection;
    private static String host, port, database, table, username, password;

    private static YamlDocument config;

    public static void openConnection() {
        config = TRANSQUE.getYamlConfig();

        host = config.getString("Database.Host");
        port = config.getString("Database.Port");
        database = config.getString("Database.Database");
        table = config.getString("Database.Table");
        username = config.getString("Database.Username");
        password = config.getString("Database.Password");

        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection MySQLDB() {return connection;}
}
