package de.fhdw.wipbank.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public class Database {
	
	private static Semaphore executeSemaphor = new Semaphore(1);

    public static Semaphore getExecuteSemaphor() {
		return executeSemaphor;
	}

	public static ResultSet query(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        //statement.close();
        //connection.close();
        return result;
    }

    public static boolean execute(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(sql);
        statement.close();
        
        connection.close();
        return rowsAffected > 0;
    }

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
