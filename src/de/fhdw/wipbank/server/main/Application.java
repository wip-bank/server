package de.fhdw.wipbank.server.main;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;

public class Application {
    public static void main(String[] args) throws Exception {
        JettyServer.run();
        
        // Hier ist ein Block Testcode :)
        //Database.execute("drop table account");
        
        AccountService accountService = new AccountService();
        accountService.createTable();
        Account account = new Account();
        account.setOwner("Test");
        account.setNumber("1001");
        accountService.create(account);
        Database.getAccounts();
    }
}
