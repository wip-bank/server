package de.fhdw.wipbank.server.main;

import java.util.List;

import javax.ws.rs.core.Response;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;
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
        account.setOwner("Jannis");
        account.setNumber("1002");
        accountService.create(account);
        List<Account> accounts = accountService.getAll();

        TransactionService transactionService = new TransactionService();
        transactionService.createTable();
//        Transaction transaction = new Transaction();
//        transaction.setSender(accounts.get(2));
//        transaction.setReceiver(accounts.get(1));
//        transaction.setAmount(BigDecimal.valueOf(1234.56));
//        transaction.setReference("Test-Transaktion");
//        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
//        transactionService.create(transaction);
        transactionService.getAll();


    }
}
