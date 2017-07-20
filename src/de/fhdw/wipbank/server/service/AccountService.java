package de.fhdw.wipbank.server.service;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;

import java.sql.Connection;
import java.sql.Statement;

public class AccountService implements Service<Account> {

    public void createTable() throws Exception {
        if (!Database.tableExists("account")) {
            Database.execute("create table account (id int not null, owner varchar(64), number varchar(4));");
        }
    }
}
