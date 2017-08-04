package de.fhdw.wipbank.server.service;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;

public class AccountService implements Service<Account> {

	@Override
    public void createTable() throws Exception {
        if (!Database.tableExists("accounts")) {
            Database.execute("create table accounts (id int not null primary key generated always as identity (start with 1, increment by 1), number varchar(4) not null unique, owner varchar(64))");
        }
    }

	@Override
	public void create(Account object) throws Exception {
		if (Database.tableExists("accounts")) {
			String insertStatement = String.format("insert into accounts (owner, number) values ('%s', '%s')", object.getOwner(), object.getNumber());
			System.out.println(insertStatement);
			Database.execute(insertStatement);
        }
	}

	@Override
	public List<Account> getAll() throws Exception {

		ResultSet resultSet = Database.query("select * from accounts");

		System.out.println("Table accounts:");

  		List<Account> accountList = new LinkedList<Account>();
  		while (resultSet.next()) {
  			//Für jeden Tabelleneintrag in der Datenbank, wird ein neues Account-Objekt erstellt
  			//und mit den Daten aus dem Eintrag befüllt.
  			Account account = new Account();
  			account.setId(resultSet.getInt(1));
  			account.setNumber(resultSet.getString(2));
  			account.setOwner(resultSet.getString(3));
  			System.out.println(String.format("ID: %s, Number: %s, Owner: %s", account.getId(), account.getNumber(), account.getOwner()));
  			//Anschließend wird das erstellt Objekt der Accountliste hinzugefügt
  			accountList.add(account);
  		}
  		return accountList;
	}

	public Account getAccount(String number) throws Exception {

		String selectStatement = String.format("select * from accounts where number = '%s'", number);
		System.out.println(selectStatement);
		ResultSet resultSet = Database.query(selectStatement);

	    if (resultSet.next()){
	    	Account account = new Account();
	    	account.setId(resultSet.getInt(1));
	    	account.setNumber(resultSet.getString(2));
	    	account.setOwner(resultSet.getString(3));

	    	// Alle Transaktionen des Accounts finden
	    	TransactionService transactionService = new TransactionService();
	    	account.setTransactions(transactionService.getTransactionsByAccount(number));

	    	return account;
	    }

		return null;
	}
}


