package de.fhdw.wipbank.server.business;

import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.TransactionService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Philipp Dyck
 */
public class GetAllTransactions {

    private TransactionService transactionService;

    public GetAllTransactions() {
        this.transactionService = new TransactionService();
    }

    /**
     * Erstellt eine Liste aller Transaktionen
     *
     * @return
     * @throws ServerException
     */
    public List<Transaction> getAll() throws ServerException {
        try {
            return transactionService.getAll();
        } catch (SQLException e) {
            throw new ServerException("Internal server error");
        }
    }
}
