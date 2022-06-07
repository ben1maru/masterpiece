package ua.masterpieces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Клас для роботи з БД
 */
public class Connector {

    public static Connection connection = null;

    /**
     * Метод підключення до БД
     */
    public static void init() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:db/database.db");
            connection.prepareStatement("PRAGMA foreign_keys = true").executeUpdate();
        } catch (SQLException ignored) { }
    }
}
