package de.fhdw.wipbank.server.util;

import de.fhdw.wipbank.server.db.Database;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Helper Methoden für Testdaten
 */
public class TestDataHelper {

    private static Logger logger = Logger.getLogger(TestDataHelper.class);

    private AccountService accountService;
    private TransactionService transactionService;

    public TestDataHelper() {
        accountService = new AccountService();
        transactionService = new TransactionService();
    }

    /**
     * Löscht alle Tabellen und setzt sie danach neu auf
     */
    public void wipeDatabase() {
        try {
            if (Database.tableExists("accounts")) {
                logger.info("Tabelle 'accounts' wird bereinigt...");
                Database.execute("drop table accounts");
            }
            if (Database.tableExists("transactions")) {
                logger.info("Tabelle 'transactions' wird bereinigt...");
                Database.execute("drop table transactions");
            }
            accountService.createTable();
            transactionService.createTable();
        } catch (Exception e) {
            logger.error("Konnte Tabellen nicht löschen");
        }
    }

    /**
     * Erstellt einen Account
     * Business Rules werden nicht beachtet
     *
     * @param owner
     * @param number
     * @return
     */
    public Account createAccount(String owner, String number) {
        try {
            Account account = new Account();
            account.setOwner(owner);
            account.setNumber(number);
            accountService.create(account);
            logger.info("TestData: " + account);
            return account;
        } catch (Exception e) {
            logger.error("Konnte folgenden Account nicht erstellen: " + owner);
            return null;
        }
    }

    /**
     * Erstellt Transaktion
     * Business Rules werden nicht beachtet
     *
     * @param sender
     * @param receiver
     * @param amount
     * @return
     */
    public Transaction createTransaction(Account sender, Account receiver, BigDecimal amount) {
        try {
            Transaction transaction = new Transaction();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setReference("Testtransaktion");
            transaction.setTransactionDate(new Date(System.currentTimeMillis()));
            transactionService.create(transaction);
            logger.info("TestData: " + transaction);
            return transaction;
        } catch (Exception e) {
            logger.error("Konnte folgende Transaktion nicht durchführen: " + amount);
            return null;
        }
    }
}
