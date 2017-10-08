package de.fhdw.wipbank.server.business;

import java.sql.SQLException;

import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.util.Validation;

/**
 * @author Philipp Dyck
 */
public class UpdateAccount {

    private AccountService accountService;

    public UpdateAccount() {
        accountService = new AccountService();
    }

    /**
     * Ändert den Namen eines Accounts
     *
     * @param number
     * @param owner
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     * @throws ServerException
     */
    public Account updateAccount(String number, String owner) throws ValidationException, NotFoundException,
            ServerException {
        checkParameters(number, owner);
        Account account = findAccountByNumber(number);
        account = changeAccount(account, owner);
        return saveAccount(account);
    }

    /**
     * Schritt 1
     * Parameter validieren
     *
     * @param number
     * @param owner
     * @throws ValidationException
     */
    private void checkParameters(String number, String owner) throws ValidationException {
        if (!Validation.isAccountNumberValid(number)) throw new ValidationException("Invalid account number");
        if (number.equals("0000")) throw new ValidationException("You cannot change the banks name");
        if (!Validation.isOwnerValid(owner)) throw new ValidationException("Invalid owner name");
    }

    /**
     * Schritt 2
     * Account finden
     *
     * @param number
     * @return
     * @throws NotFoundException
     * @throws ServerException
     */
    private Account findAccountByNumber(String number) throws NotFoundException, ServerException {
        try {
            Account account = accountService.getAccount(number);
            if (account != null) {
                return account;
            } else {
                throw new NotFoundException("Account not found");
            }
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }

    /**
     * Schritt 3
     * Änderungen am Account durchführen
     *
     * @param account
     * @param owner
     * @return
     */
    private Account changeAccount(Account account, String owner) {
        account.setOwner(owner);
        return account;
    }

    /**
     * Schritt 4
     * Daten in der Datenbank anpassen
     *
     * @param account
     * @return
     * @throws ServerException
     */
    private Account saveAccount(Account account) throws ServerException {
        try {
            return accountService.update(account);
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }
}
