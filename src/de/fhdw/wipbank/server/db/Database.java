package de.fhdw.wipbank.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {


    /**
     * Eine Anfrage an die Datenbank senden
     *
     * @param sql
     * @return
     * @throws SQLException
     */
	public static ResultSet query(String sql) throws SQLException {
        Connection connection = getConnection();
    	connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        //statement.close();
        //connection.close();
        return result;
    }

    /**
     * Einen SQL Befehl auf der Datenbank ausführen
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static boolean execute(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(sql);
        statement.close();

        connection.close();
        return rowsAffected > 0;
    }

    /**
     * Prüft, ob die Tabelle in der Datenbank existiert
     *
     * @param table
     * @return
     */
    public static boolean tableExists(String table) {
        try {
            Connection connection = getConnection();
            ResultSet resultSet = connection.getMetaData().getTables("%", "%", "%", new String[] { "TABLE" });
            boolean tableExists = false;
            while (resultSet.next() && !tableExists) {
                if (resultSet.getString("TABLE_NAME").equalsIgnoreCase(table)) {
                    tableExists = true;
                }
            }
            resultSet.close();
            return tableExists;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Stellt eine Verbindung zur Datenbank her
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:derby:database;create=true", getCredentials());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Properties getCredentials() {
        Properties properties = new Properties();
        properties.put("user", "user");
        return properties;
    }
}
