package de.fhdw.wipbank.server.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


import de.fhdw.wipbank.server.model.Account;

public class Database {

    public static ResultSet query(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        statement.close();
        connection.close();
        return result;
    }

    public static boolean execute(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        boolean result = statement.execute(sql);
        statement.close();
        connection.close();
        return result;
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
    
  //Gibt eine Liste von Accounts
  	public static List<Account> getAccounts() throws SQLException {
  		//Aufbau einer Verbindung zur Datenbank
  		Connection connection = getConnection();
  		Statement statement = connection.createStatement();
  		String sql = "SELECT * FROM account";
  		//Absetzen eines SQL-Statements, um alle Accounts aus der Datenbank zu lesen
  		ResultSet resultSet = statement.executeQuery(sql);
  		System.out.println("Table account:");
  		//Erstellung einer Liste, in der alle Accounts aus der Datenbank gesammelt werden sollen
  		List<Account> accountList = new LinkedList<Account>();
  		while (resultSet.next()) {
  			//Für jeden Tabelleneintrag in der Datenbank, wird ein neues Studenten-Objekt erstellt
  			//und mit den Daten aus dem Eintrag befüllt
  			Account account = new Account();
  			account.setId(resultSet.getInt(1));
  			account.setOwner(resultSet.getString(2));
  			account.setNumber(resultSet.getString(3));
			System.out.println(String.format("ID: %s, Number: %s, Owner: %s", account.getId(), account.getOwner(), account.getNumber()));
  			//Anschließend wird das erstellt Objekt der Accountliste hinzugefügt
  			accountList.add(account);
  		}
  		//Connection, Statement, ResultSet schließen (evtl. Fehler noch abfangen, ggf. nicht schließen etc.)
  		resultSet.close();
  		statement.close();
  		connection.close();
  		return accountList;
  	}

}
