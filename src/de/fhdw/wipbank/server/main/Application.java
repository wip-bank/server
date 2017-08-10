package de.fhdw.wipbank.server.main;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import de.fhdw.wipbank.server.db.Database;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

public class Application {
    public static void main(String[] args) throws Exception {
        JettyServer.run();

        // Hier ist ein Block Testcode :) Testcommit
        Database.execute("drop table transactions");

        AccountService accountService = new AccountService();
//        accountService.createTable();
//        Account account = new Account();
//        account.setOwner("Alex");
//        account.setNumber("1003");
//        accountService.create(account);
        List<Account> accounts = accountService.getAll();

        TransactionService transactionService = new TransactionService();
        transactionService.createTable();
        
        Transaction transaction = new Transaction();
        transaction.setSender(accounts.get(0));
        transaction.setReceiver(accounts.get(1));
        transaction.setAmount(BigDecimal.valueOf(100000.00));
        transaction.setReference("Testtransaktion1");
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        transactionService.create(transaction);
        
        transaction = new Transaction();
        transaction.setSender(accounts.get(0));
        transaction.setReceiver(accounts.get(2));
        transaction.setAmount(BigDecimal.valueOf(100000.00));
        transaction.setReference("Testtransaktion2");
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        transactionService.create(transaction);
        
        transaction = new Transaction();
        transaction.setSender(accounts.get(0));
        transaction.setReceiver(accounts.get(3));
        transaction.setAmount(BigDecimal.valueOf(100000.00));
        transaction.setReference("Testtransaktion3");
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        transactionService.create(transaction);
//        
        transactionService.getAll();
        
        
    }
    
    
 
   
}
