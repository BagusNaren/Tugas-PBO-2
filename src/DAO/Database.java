package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    static {
        try {
            Class.forName("org.sqlite.JDBC"); // Load driver SQLite
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        System.out.println(">> Connecting to SQLite database: vbook.db");
        return DriverManager.getConnection("jdbc:sqlite:database/vbook.db");
    }

}
