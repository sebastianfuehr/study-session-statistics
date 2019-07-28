package de.berlin.vivepassion.io;

import de.berlin.vivepassion.VPSConfiguration;
import de.berlin.vivepassion.VPStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {

    /**
     * Connects to the database vpstats_db. The path for the database is specified
     * in the vivepassionstats.properties. If the database is not yet present, a new
     * database is created.
     */
    public static void testConnection() {
        Connection connection = null;
        try {
            // db parameters
            String url = VPSConfiguration.properties().getProperty("db_url");
            // create a connection to the database
            connection = DriverManager.getConnection(url);
            if (VPStats.debugMode())
                System.out.println(String.format("Connection with database at %s successful.", url));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
