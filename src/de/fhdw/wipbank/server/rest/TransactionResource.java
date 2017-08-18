package de.fhdw.wipbank.server.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.ExecuteTransaction;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.PreconditionFailedException;
import de.fhdw.wipbank.server.exception.ValidationException;

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
            logger.info("POST /transaction [sender: " + senderNumber + ", receiver: " + receiverNumber + ", amount: " +
					amount + "]");
            (new ExecuteTransaction()).executeTransaction(senderNumber, receiverNumber, amount, reference);
            return Response.ok().build();
        } catch (ValidationException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.notFound(e.getMessage());
        } catch (PreconditionFailedException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.preconditionFailed(e.getMessage());
        }
    }
}
