package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Singleton
public class GeneralResource {

	// Auskommentiert da nicht in der Application.wadl von Sebastion Scholz
	// Trotzdem sinnvoll ... :D
	// @GET
	// @Path("/health")
	// @Produces({ MediaType.TEXT_PLAIN + "; charset=utf-8" })
	// public Response health() {
	// return Response.ok("ok").build();
	// }
	@GET
	@Path("/account/{number}")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
	public Response getAccount(@PathParam("number") String number) {
		Account account = new Account();
		AccountService accountService = new AccountService();
		try {
			account = accountService.getAccount(number);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok(account).build();
	}


	@POST
	@Path("/transaction")
	 @Consumes("application/x-www-form-urlencoded")
	/**
	 * https://stackoverflow.com/a/8194612
	 * @param senderNumber
	 * @param receiverNumber
	 * @param amount
	 * @param reference
	 * @return
	 */
	public Response executeTransaction(@FormParam("senderNumber") String senderNumber,
			@FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount,
			@FormParam("reference") String reference) {

		double tAmount = Double.valueOf(amount);

		Transaction transaction = new Transaction();
		Account sender = new Account();
		sender.setNumber(senderNumber);
		transaction.setSender(sender);
		Account receiver = new Account();
		receiver.setNumber(receiverNumber);
		transaction.setReceiver(receiver);
		transaction.setAmount(BigDecimal.valueOf(tAmount));
		transaction.setReference(reference);
		transaction.setTransactionDate(new Date(System.currentTimeMillis()));
		TransactionService transactionService = new TransactionService();
		try {
			transactionService.create(transaction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build(); // Keine Ahnung ob das
												   // richtig ist.
			// Wahrscheinlich muss man hier alle möglichen Fehlerfälle abfangen
			// und mit responsecode zurückgeben
			// (Siehe Schnittstelle)
		}
		return Response.ok().build();

	}

}
