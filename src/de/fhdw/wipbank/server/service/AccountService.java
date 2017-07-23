package de.fhdw.wipbank.server.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;

public class AccountService implements Service<Account> {

    public void createTable() throws Exception {
        if (!Database.tableExists("account")) {
            Database.execute("create table account (id int not null primary key generated always as identity (start with 1, increment by 1), number varchar(4) not null unique, owner varchar(64))");
        }
    }

	@Override
	public void create(Account object) throws Exception {
		if (Database.tableExists("account")) {
			String insertStatement = String.format("insert into account (owner, number) values ('%s', '%s')", object.getOwner(), object.getNumber());
			System.out.println(insertStatement);
			Database.execute(insertStatement);
        }
	}

	@Override
	public List<Account> getAll() throws SQLException {
		
		ResultSet resultSet = Database.query("select * from account");
		
		System.out.println("Table account:");
  		
  		List<Account> accountList = new LinkedList<Account>();
  		while (resultSet.next()) {
  			//Für jeden Tabelleneintrag in der Datenbank, wird ein neues Account-Objekt erstellt
  			//und mit den Daten aus dem Eintrag befüllt
  			Account account = new Account();
  			account.setId(resultSet.getInt(1));
  			account.setOwner(resultSet.getString(2));
  			account.setNumber(resultSet.getString(3));
			System.out.println(String.format("ID: %s, Number: %s, Owner: %s", account.getId(), account.getOwner(), account.getNumber()));
  			//Anschließend wird das erstellt Objekt der Accountliste hinzugefügt
  			accountList.add(account);
  		}
  		return accountList;
	}
}


