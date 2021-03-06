package de.fhdw.wipbank.server.business;

import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.util.Validation;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Philipp Dyck
 */
public class CreateAccount {

    private AccountService accountService;

    public CreateAccount() {
        accountService = new AccountService();
    }

    /**
     * Erstellt einen neuen Account und vergibt eine Kontonummer
     *
     * @param owner
     * @return
     * @throws ValidationException
     * @throws ServerException
     */
    public Account createAccount(String owner) throws ValidationException, ServerException {
        checkParamter(owner);
        String number = determineNumber();
        return create(owner, number);
    }

    /**
     * Schritt 1
     * Parameter validieren
     *
     * @param owner
     * @throws ValidationException
     */
    private void checkParamter(String owner) throws ValidationException{
        if (!Validation.isOwnerValid(owner)) {
            throw new ValidationException("Invalid owner name");
        }
    }

    /**
     * Schritt 2
     * Nächste Account number bestimmen
     *
     * @return
     * @throws ServerException
     */
    private String determineNumber() throws ServerException {
        try {
            List<Account> accounts = accountService.getAll();
            if (isFirstAccount(accounts)) {
                return "1000";
            }
            List<Integer> numbers = accounts.stream()
                    .map(a -> Integer.parseInt(a.getNumber()))
                    .collect(Collectors.toList());
            Integer max = Collections.max(numbers);
            Integer number = max + 1;
            return number.toString();
        } catch (Exception e) {
            throw new ServerException("Internal server error");
        }
    }

    /** Pr�ft, ob der n�chste Account, der erste Account nach dem Bankkonto ist.
     * @param accounts Liste aller bereits vorhandenen Accounts
     * @return Wenn die Liste der Accounts max. 1 Eintrag besitzt, so ist der n�chste Account der erste Account (true).
     */
    private boolean isFirstAccount(List<Account> accounts) {
        return accounts.size() <= 1;
    }

    /**
     * Schritt 3
     * Account erstellen
     *
     * @param owner
     * @param number
     * @return
     * @throws ServerException
     */
    private Account create(String owner, String number) throws ServerException {
        try {
            Account account = buildAccount(owner, number);
            accountService.create(account);
            return account;
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }

    private Account buildAccount(String owner, String number) {
        Account account = new Account();
        account.setOwner(owner);
        account.setNumber(number);
        return account;
    }
}
