package de.fhdw.wipbank.server.business;

import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;

import java.sql.SQLException;
import java.util.List;

public class GetAllAccounts {

    private AccountService accountService;

    public GetAllAccounts() {
        accountService = new AccountService();
    }

    /**
     * Erstellt eine Liste aller Accounts
     *
     * @return
     * @throws ServerException
     */
    public List<Account> getAll() throws ServerException {
        try {
            return accountService.getAll();
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }
}
