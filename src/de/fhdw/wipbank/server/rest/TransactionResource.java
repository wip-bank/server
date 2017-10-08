package de.fhdw.wipbank.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.ExecuteTransaction;
import de.fhdw.wipbank.server.business.GetAllTransactions;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.PreconditionFailedException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;

import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.model.TransactionList;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Philipp Dyck
 */
@Path("/transaction")
@Singleton
public class TransactionResource {

	private static Logger logger = Logger.getLogger(TransactionResource.class);

    /**
     * Führt eine Transaktion durch
     *
     * @param senderNumber
     * @param receiverNumber
     * @param amount
     * @param reference
     * @return
     */
	@POST
	@Path("/")
	public synchronized Response executeTransaction(
	        @FormParam("senderNumber") String senderNumber,
			@FormParam("receiverNumber") String receiverNumber,
            @FormParam("amount") String amount,
			@FormParam("reference") String reference) {
        try {
            logger.info("POST /transaction [sender: " + senderNumber + ", receiver: " + receiverNumber + ", amount: " +
					amount + "]");
            (new ExecuteTransaction()).executeTransaction(senderNumber, receiverNumber, amount, reference);
            return Response.ok().build();
        } catch (ValidationException e) {
            logger.error("400 " + e.getMessage());
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            logger.error("404 " + e.getMessage());
            return ResponseBuilder.notFound(e.getMessage());
        } catch (PreconditionFailedException e) {
            logger.error("412 " + e.getMessage());
            return ResponseBuilder.preconditionFailed(e.getMessage());
        } catch (ServerException e) {
            logger.error("500 " + e.getMessage());
            return ResponseBuilder.error(e.getMessage());
        }
    }

    /**
     * Gibt alle Transaktionen zurück
     *
     * @return
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAll() {
        try {
            logger.info("GET /transaction");
            List<Transaction> transactions = (new GetAllTransactions()).getAll();
            return ResponseBuilder.ok(wrapTransactions(transactions));
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.error(e.getMessage());
        }
    }

    private TransactionList wrapTransactions(List<Transaction> transactions) {
        TransactionList list = new TransactionList();
        list.setList(transactions);
        return list;
    }
}
