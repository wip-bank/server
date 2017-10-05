package de.fhdw.wipbank.server.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.fhdw.wipbank.server.db.Database;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;

public class TransactionService {

    private static final String INSERT_TRANSACTION = "insert into transactions (senderNumber, receiverNumber, amount, reference, transactionDate) values (?, ?, ?, ?, ?)";

	/**
	 * Erstellt die Transaktions Tabelle, falls sie noch nicht existiert
	 *
	 * @throws SQLException
	 */
	public void createTable() throws SQLException {
		if (!Database.tableExists("transactions")) {
			Database.execute(
					"create table transactions (id int not null primary key generated always as identity (start with 1, increment by 1), senderNumber varchar(4) not null, receiverNumber varchar(4) not null, amount decimal(20, 2), reference varchar(64), transactionDate timestamp )");
		}
	}

	/**
	 * Erstellt eine neue Transaktion in der Datenbank
	 *
	 * @param transaction
	 * @return
	 * @throws SQLException
	 */
	public boolean create(Transaction transaction) throws SQLException {
        PreparedStatement insert = Database.getConnection().prepareStatement(INSERT_TRANSACTION);
        insert.setString(1, transaction.getSender().getNumber());
        insert.setString(2, transaction.getReceiver().getNumber());
        insert.setBigDecimal(3, transaction.getAmount());
        insert.setString(4, transaction.getReference());
        insert.setString(5, new Timestamp(transaction.getTransactionDate().getTime()).toString());
        insert.execute();
        insert.close();
        return true;
	}

	/**
	 * Sucht nach allen Transaktionen in der Datenbank
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<Transaction> getAll() throws SQLException {
		ResultSet resultSet = Database.query(
				"select T1.id, T1.senderNumber, T1.receiverNumber, T1.amount, T1.reference, T1.transactionDate, T2.owner as senderOwner, T3.owner as receiverOwner from transactions as T1 join accounts as T2 on T1.senderNumber = T2.number join accounts as T3 on T1.receiverNumber = T3.number order by transactionDate desc ");


		List<Transaction> transactionList = new LinkedList<Transaction>();
		while (resultSet.next()) {
			Transaction transaction = new Transaction();
			transaction.setId(resultSet.getInt(1));

			Account sender = new Account();
			sender.setNumber(resultSet.getString(2));
			sender.setOwner(resultSet.getString(7));
			transaction.setSender(sender);

			Account receiver = new Account();
			receiver.setNumber(resultSet.getString(3));
			receiver.setOwner(resultSet.getString(8));
			transaction.setReceiver(receiver);

			transaction.setAmount(resultSet.getBigDecimal(4));
			transaction.setReference(resultSet.getString(5));
			transaction.setTransactionDate(new Date(resultSet.getTimestamp(6).getTime()));
			transactionList.add(transaction);
		}
		return transactionList;
	}

	/**
	 * Sucht nach allen Transaktionen eines bestimmten Accounts
	 *
	 * @param number
	 * @return
	 * @throws SQLException
	 */
	public List<Transaction> getTransactionsByAccount(String number) throws SQLException {

		String selectStatement = String.format(
				"select T1.id, T1.senderNumber, T1.receiverNumber, T1.amount, T1.reference, T1.transactionDate, T2.owner as senderOwner, T3.owner as receiverOwner from transactions as T1 join accounts as T2 on T1.senderNumber = T2.number join accounts as T3 on T1.receiverNumber = T3.number where senderNumber = '%s' or receiverNumber = '%s' order by transactionDate asc",
				number, number);
		ResultSet resultSet = Database.query(selectStatement);

		List<Transaction> transactionList = new LinkedList<Transaction>();
		while (resultSet.next()) {
			Transaction transaction = new Transaction();
			transaction.setId(resultSet.getInt(1));

			Account sender = new Account();
			sender.setNumber(resultSet.getString(2));
			sender.setOwner(resultSet.getString(7));
			transaction.setSender(sender);

			Account receiver = new Account();
			receiver.setNumber(resultSet.getString(3));
			receiver.setOwner(resultSet.getString(8));
			transaction.setReceiver(receiver);

			transaction.setAmount(resultSet.getBigDecimal(4));
			transaction.setReference(resultSet.getString(5));
			transaction.setTransactionDate(new Date(resultSet.getTimestamp(6).getTime()));
			transactionList.add(transaction);
		}
		return transactionList;
	}

}
