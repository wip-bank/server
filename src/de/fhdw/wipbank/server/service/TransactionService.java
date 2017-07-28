package de.fhdw.wipbank.server.service;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;

public class TransactionService implements Service<Transaction>{

	@Override
	public void createTable() throws Exception {
		if (!Database.tableExists("transactions")) {
            Database.execute("create table transactions (id int not null primary key generated always as identity (start with 1, increment by 1), senderNumber varchar(4) not null, receiverNumber varchar(4) not null, amount decimal(15, 2), reference varchar(64), transactionDate timestamp )");
        }
	}

	@Override
	public void create(Transaction object) throws Exception {
		if (Database.tableExists("transactions")) {
			String insertStatement = String.format("insert into transactions (senderNumber, receiverNumber, amount, reference, transactionDate) values ('%s', '%s', %s, '%s', '%s')", object.getSender().getNumber(), object.getReceiver().getNumber(), object.getAmount(), object.getReference(), new java.sql.Timestamp(object.getTransactionDate().getTime()));
			System.out.println(insertStatement);
			Database.execute(insertStatement);
        }
	}

	@Override
	public List<Transaction> getAll() throws Exception {
		ResultSet resultSet = Database.query("select T1.id, T1.senderNumber, T1.receiverNumber, T1.amount, T1.reference, T1.transactionDate, T2.owner as senderOwner, T3.owner as receiverOwner from transactions as T1 join accounts as T2 on T1.senderNumber = T2.number join accounts as T3 on T1.receiverNumber = T3.number ");
		
		System.out.println("Table transactions:");
  		
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
			System.out.println(String.format("ID: %s, Sender: %s, Receiver: %s, Amount: %s, Reference: %s, TransactionDate: %s", transaction.getId(), transaction.getSender().getNumber(), transaction.getReceiver().getNumber(), transaction.getAmount(), transaction.getReference(), transaction.getTransactionDate()));
  			transactionList.add(transaction);
  		}
  		return transactionList;
	}

	public List<Transaction> getTransactionsByAccount(String number) throws Exception {
	
		String selectStatement = String.format("select T1.id, T1.senderNumber, T1.receiverNumber, T1.amount, T1.reference, T1.transactionDate, T2.owner as senderOwner, T3.owner as receiverOwner from transactions as T1 join accounts as T2 on T1.senderNumber = T2.number join accounts as T3 on T1.receiverNumber = T3.number where senderNumber = '%s' or receiverNumber = '%s'", number, number);
		System.out.println(selectStatement);
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
			System.out.println(String.format("ID: %s, Sender: %s, Receiver: %s, Amount: %s, Reference: %s, TransactionDate: %s", transaction.getId(), transaction.getSender().getNumber(), transaction.getReceiver().getNumber(), transaction.getAmount(), transaction.getReference(), transaction.getTransactionDate()));
  			transactionList.add(transaction);
  		}
  		return transactionList;
	}
	
}
