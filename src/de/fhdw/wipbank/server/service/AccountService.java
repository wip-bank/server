package de.fhdw.wipbank.server.service;

import de.fhdw.wipbank.server.database.Database;
import de.fhdw.wipbank.server.model.Account;

public class AccountService implements Service<Account> {

    public void createTable() throws Exception {
        if (!Database.tableExists("account")) {
            Database.execute("create table account (id int not null primary key generated always as identity (start with 1, increment by 1), number varchar(4) not null unique, owner varchar(64))");
        }
    }

	@Override
	public void create(Account object) throws Exception {
		if (Database.tableExists("account")) {
			String insertStatement = String.format("insert into account (owner, number) values ('%s', '%s')", object.getOwner(), object.getNumber());
			System.out.println(insertStatement);
			Database.execute(insertStatement);
        }
	}
}


