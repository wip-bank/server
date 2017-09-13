package de.fhdw.wipbank.server.main;

import java.math.BigDecimal;
import java.util.List;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.util.TestDataHelper;

public class Application {

    public static void main(String[] args) throws Exception {
        JettyServer.run();
        initTestData();
    }

    private static void initTestData() {
        TestDataHelper helper = new TestDataHelper();
        helper.wipeDatabase();
        Account bank = helper.createAccount("Bank", "0000");
        Account daniel = helper.createAccount("Daniel", "1000");
        Account philipp = helper.createAccount("Philipp", "1001");
        Account jannis = helper.createAccount("Jannis", "1002");
        Account alexander = helper.createAccount("Alexander", "1003");
        helper.createTransaction(bank, daniel, BigDecimal.valueOf(10000.00));
        helper.createTransaction(bank, philipp, BigDecimal.valueOf(10000.00));
        helper.createTransaction(bank, jannis, BigDecimal.valueOf(10000.00));
        helper.createTransaction(bank, alexander, BigDecimal.valueOf(10000.00));
    }

}
