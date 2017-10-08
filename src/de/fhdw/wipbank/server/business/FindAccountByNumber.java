package de.fhdw.wipbank.server.business;

import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.util.Validation;

/**
 * @author Philipp Dyck
 */
public class FindAccountByNumber {

    private AccountService accountService;

    public FindAccountByNumber() {
        accountService = new AccountService();
    }

    /**
     * Sucht nach einem Account mit der angegebenen Kontonummer
     *
     * @param number
     * @return
     * @throws ValidationException
     * @throws NotFoundException
     */
    public Account findAccountByNumber(String number) throws ValidationException, NotFoundException {
        checkParameter(number);
        return getAccount(number);
    }

    /**
     * Schritt 1
     * Account number valdieren
     *
     * @param number
     * @throws ValidationException
     */
    private void checkParameter(String number) throws ValidationException {
        if (!Validation.isAccountNumberValid(number)) throw new ValidationException("Invalid account number");
        if (number.equals("0000")) throw new ValidationException("You cannot login as 0000");
    }

    /**
     * Schritt 2
     * Account finden
     *
     * @param number
     * @return
     * @throws NotFoundException
     */
    private Account getAccount(String number) throws NotFoundException {
        try {
            Account account = accountService.getAccount(number);
            if (account == null) {
                throw new NotFoundException("Account not found");
            }
            return account;
        } catch (Exception e) {
            throw new NotFoundException("Account not found");
        }
    }
}
