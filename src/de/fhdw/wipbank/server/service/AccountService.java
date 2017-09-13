package de.fhdw.wipbank.server.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.wipbank.server.db.Database;
import de.fhdw.wipbank.server.model.Account;

public class AccountService implements Service<Account> {

	private static final String TABLE_NAME = "accounts";
	private static final String CREATE_TABLE = "create table accounts (id int not null primary key generated always as identity (start with 1, increment by 1), number varchar(4) not null unique, owner varchar(64))";
    private static final String INSERT_ACCOUNT = "insert into accounts (owner, number) values (?, ?)";
	private static final String UPDATE_ACCOUNT = "update accounts set owner = ? where number = ?";

	@Override
    public void createTable() throws SQLException {
        if (!Database.tableExists(TABLE_NAME)) {
            Database.execute(CREATE_TABLE);
        }
    }

	@Override
	public boolean create(Account account) throws SQLException {
        PreparedStatement insert = Database.getConnection().prepareStatement(INSERT_ACCOUNT);
        insert.setString(1, account.getOwner());
        insert.setString(2, account.getNumber());
        insert.execute();
		return true;
	}

	@Override
	public List<Account> getAll() throws SQLException {
		ResultSet results = Database.query("select * from accounts");
  		List<Account> accountList = new ArrayList<Account>();
  		while (results.next()) {
            Account account = convertToAccount(results);
  			accountList.add(account);
  		}
  		return accountList;
	}

	public Account getAccount(String number) throws SQLException {

		String selectStatement = String.format("select * from accounts where number = '%s'", number);
//		System.out.println(selectStatement);
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

	public Account update(Account account) throws SQLException {
		PreparedStatement update = Database.getConnection().prepareStatement(UPDATE_ACCOUNT);
		update.setString(1, account.getOwner());
		update.setString(2, account.getNumber());
		update.executeUpdate();
        update.close();
		return account;
	}

	private Account convertToAccount(ResultSet results) throws SQLException {
        Account account = new Account();
        account.setId(results.getInt(1));
        account.setNumber(results.getString(2));
        account.setOwner(results.getString(3));
        return account;
    }
}


