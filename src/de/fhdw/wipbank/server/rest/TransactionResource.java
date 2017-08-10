package de.fhdw.wipbank.server.rest;

import java.math.BigDecimal;

import java.util.Date;
import java.util.concurrent.Semaphore;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.ExecuteTransaction;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.PreconditionFailedException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

import de.fhdw.wipbank.server.util.Validation;
import org.apache.log4j.Logger;


@Path("/transaction")
@Singleton
public class TransactionResource {

	private static Logger logger = Logger.getLogger(TransactionResource.class);

	@POST
	@Path("/")
	/**
	 * https://stackoverflow.com/a/8194612
	 * 
	 * @param senderNumber
	 * @param receiverNumber
	 * @param amount
	 * @param reference
	 * @return
	 */
	public Response executeTransaction(
	        @FormParam("senderNumber") String senderNumber,
			@FormParam("receiverNumber") String receiverNumber,
            @FormParam("amount") String amount,
			@FormParam("reference") String reference) {
        try {
            logger.info("executeTransaction: " + senderNumber + " -> " + receiverNumber + " sends " + amount);
            (new ExecuteTransaction()).executeTransaction(senderNumber, receiverNumber, amount, reference);
            return Response.ok().build();
        } catch (ValidationException e) {
            return buildResponse(Status.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            return buildResponse(Status.NOT_FOUND, e.getMessage());
        } catch (PreconditionFailedException e) {
            return buildResponse(Status.PRECONDITION_FAILED, e.getMessage());
        }
    }

	private Response buildResponse(Status status, String msg) {
        logger.error("executeTransaction[status: " + status.getStatusCode() + "; message: '" + msg + "']");
		return Response.status(status).entity(msg).build();
	}

}
