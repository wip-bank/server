package de.fhdw.wipbank.server.main;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

public class Application {
    public static void main(String[] args) throws Exception {
        JettyServer.run();

        // Hier ist ein Block Testcode :) Testcommit
        //Database.execute("drop table account");

        AccountService accountService = new AccountService();
        accountService.createTable();
        Account account = new Account();
        account.setOwner("Alex");
        account.setNumber("1003");
        accountService.create(account);
        List<Account> accounts = accountService.getAll();

        TransactionService transactionService = new TransactionService();
        transactionService.createTable();
//        Transaction transaction = new Transaction();
//        transaction.setSender(accounts.get(1));
//        transaction.setReceiver(accounts.get(3));
//        transaction.setAmount(BigDecimal.valueOf(30000000.00));
//        transaction.setReference("R�ck�berweisung");
//        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
//        transactionService.create(transaction);
        transactionService.getAll();


    }
}
