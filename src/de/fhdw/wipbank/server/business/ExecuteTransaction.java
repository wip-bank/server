package de.fhdw.wipbank.server.business;

import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.PreconditionFailedException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;
import de.fhdw.wipbank.server.util.Validation;

import java.math.BigDecimal;
import java.util.Date;

public class ExecuteTransaction {

    private AccountService accountService;
    private TransactionService transactionService;

    public ExecuteTransaction() {
        accountService = new AccountService();
        transactionService = new TransactionService();
    }

    /**
     * Transaktion durchführen
     *
     * @param senderNumber
     * @param receiverNumber
     * @param amount
     * @param reference
     * @throws ValidationException
     * @throws NotFoundException
     */
    public synchronized void executeTransaction(String senderNumber, String receiverNumber, String amount,
            String reference) throws ValidationException, NotFoundException, PreconditionFailedException {
        checkParameters(senderNumber, receiverNumber, amount, reference);
        Account sender = findAccountByNumber(senderNumber);
        Account receiver = findAccountByNumber(receiverNumber);
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
        if (!Validation.isReferenceValid(reference)) throw new ValidationException("Invalid reference");
        if (!Validation.isAmountValid(amount)) throw new ValidationException("Invalid amount");
        if (!Validation.isAccountNumberValid(senderNumber)) throw new ValidationException("Invalid sender");
        if (!Validation.isAccountNumberValid(receiverNumber)) throw new ValidationException("Invalid receiver");
        if (senderNumber.equals(receiverNumber)) throw new ValidationException("Sender and receiver cannot be equal");
    }

    /**
     * Schritt 2
     * Sender und Receiver finden
     *
     * @param number
     * @return Account
     * @throws NotFoundException
     */
    private Account findAccountByNumber(String number) throws NotFoundException {
        try {
            Account account = accountService.getAccount(number);
            if (account == null) {
                throw new NotFoundException(number + " not found");
            }
            return account;
        } catch (Exception e) {
            throw new NotFoundException(number + " not found");
        }
    }

    /**
     * Schritt 3
     * Transaktion erstellen
     *
     * @param sender
     * @param receiver
     * @param amountString
     * @param reference
     * @throws PreconditionFailedException
     */
    private void createTransaction(Account sender, Account receiver, String amountString, String reference)
            throws PreconditionFailedException {
        BigDecimal amount = convertAmount(amountString);
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setReference(reference);
        transaction.setTransactionDate(new Date(System.currentTimeMillis()));
        try {
            boolean success = transactionService.create(transaction);
            if (!success) {
                throw new PreconditionFailedException("Insufficient solvency");
            }
        } catch (Exception e) {
            throw new PreconditionFailedException("Insufficient solvency");
        }
    }

    private BigDecimal convertAmount(String amount) {
        return BigDecimal.valueOf(Double.valueOf(amount));
    }
}
