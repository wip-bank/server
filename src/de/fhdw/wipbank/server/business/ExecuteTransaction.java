package de.fhdw.wipbank.server.business;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.PreconditionFailedException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;
import de.fhdw.wipbank.server.util.Validation;

/**
 * @author Philipp Dyck
 */
public class ExecuteTransaction {

    private AccountService accountService;
    private TransactionService transactionService;

    public ExecuteTransaction() {
        accountService = new AccountService();
        transactionService = new TransactionService();
    }

    /**
     * Führt eine atomare Transaktion durch
     *
     * @param senderNumber
     * @param receiverNumber
     * @param amount
     * @param reference
     * @throws ValidationException
     * @throws NotFoundException
     */
    public synchronized void executeTransaction(String senderNumber, String receiverNumber, String amount,
            String reference) throws ValidationException, NotFoundException, PreconditionFailedException,
            ServerException {
        checkParameters(senderNumber, receiverNumber, amount, reference);
        Account sender = findAccountByNumber(senderNumber);
        Account receiver = findAccountByNumber(receiverNumber);
        checkSolvency(sender, amount);
        createTransaction(sender, receiver, amount, reference);
    }

    /**
     * Schritt 1
     * Parameter prüfen
     *
     * @param senderNumber
     * @param receiverNumber
     * @param amount
     * @param reference
     * @throws ValidationException
     */
    private void checkParameters(String senderNumber, String receiverNumber, String amount, String reference)
            throws ValidationException {
        if (!Validation.isAccountNumberValid(senderNumber)) throw new ValidationException("Invalid sender");
        if (!Validation.isAccountNumberValid(receiverNumber)) throw new ValidationException("Invalid receiver");
        if (senderNumber.equals(receiverNumber)) throw new ValidationException("Sender and receiver cannot be equal");
        if (!Validation.isAmountValid(amount)) throw new ValidationException("Invalid amount");
        if (!Validation.isReferenceValid(reference)) throw new ValidationException("Invalid reference");
    }

    /**
     * Schritt 2
     * Sender und Receiver finden
     *
     * @param number
     * @return Account
     * @throws NotFoundException
     */
    private Account findAccountByNumber(String number) throws NotFoundException, ServerException {
        try {
            Account account = accountService.getAccount(number);
            if (account == null) {
                throw new NotFoundException(number + " not found");
            }
            return account;
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }

    /**
     * Schritt 3
     * Liquidität eines Kontos prüfen
     *
     * @param account
     * @throws PreconditionFailedException
     */
    private void checkSolvency(Account account, String amountString) throws PreconditionFailedException {
        if (isRegularAccount(account)) {
            BigDecimal balance = calculateBalance(account);
            checkCondition(balance, convertAmount(amountString));
        }
    }

    private boolean isRegularAccount(Account account) {
        return !account.getNumber().equals("0000");
    }

    private BigDecimal calculateBalance(Account account) {
        return account.getTransactions().stream().map(t -> {
            if (t.getReceiver().getNumber().equals(account.getNumber())) {
                return t.getAmount();
            } else {
                return t.getAmount().negate();
            }
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void checkCondition(BigDecimal balance, BigDecimal amount) throws PreconditionFailedException {
        if (balance.compareTo(amount) < 0) throw new PreconditionFailedException("Insufficient solvency");
    }

    /**
     * Schritt 4
     * Transaktion erstellen
     *
     * @param sender
     * @param receiver
     * @param amountString
     * @param reference
     * @throws PreconditionFailedException
     */
    private void createTransaction(Account sender, Account receiver, String amountString, String reference)
            throws PreconditionFailedException, ServerException {
        Transaction transaction = buildTransaction(sender, receiver, convertAmount(amountString), reference);
        try {
            boolean success = transactionService.create(transaction);
            if (!success) {
                throw new PreconditionFailedException("Insufficient solvency");
            }
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }

    private Transaction buildTransaction(Account sender, Account receiver, BigDecimal amount, String reference) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setReference(reference);
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        return transaction;
    }

    private BigDecimal convertAmount(String amount) {
        return BigDecimal.valueOf(Double.valueOf(amount));
    }
}
