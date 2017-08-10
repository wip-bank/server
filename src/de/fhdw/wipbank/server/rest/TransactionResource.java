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

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

import de.fhdw.wipbank.server.util.Validation;
import org.apache.log4j.Logger;


@Path("/transaction")
@Singleton
public class TransactionResource {
	
	private static Semaphore executeTransactionSemaphore = new Semaphore(1);

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
	public Response executeTransaction(@FormParam("senderNumber") String senderNumber,
			@FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount,
			@FormParam("reference") String reference) {
		
		try {
			executeTransactionSemaphore.acquire();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			return Response.serverError().build(); 
		}
		
		Logger logger = Logger.getLogger(getClass());
		
		logger.info( "REST: executeTransaction" + 
					 " senderNumber: " + senderNumber + 
					 " receiverNumber: " + receiverNumber + 
					 " amount: " + amount + 
					 " reference: " + reference); 

		// Fehlerpr�fungen

		// 1. -> Erlaubte Zeichen bei reference: A-Z, a-z, 0-9, Leerzeichen
		// (Pflichtfeld)
		if (!Validation.isReferenceValid(reference))
			return getResponse(Response.Status.BAD_REQUEST, "Referenz ung�ltig");

		// 2. -> Format amount: 2 Nachkommastellen durch "." getrennt (z.B. 1234.99) -
		// Angaben wie 1234 statt 1234.00 sind ebenfalls erlaubt (Pflichtfeld)
		if (!Validation.isAmountValid(amount))
			return getResponse(Response.Status.BAD_REQUEST, "Betrag ung�ltig");

		// 3. -> Format receiverNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(receiverNumber))
			return getResponse(Response.Status.BAD_REQUEST, "Empf�nger ung�ltig");

		// 4. -> Format senderNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(senderNumber))
			return getResponse(Response.Status.BAD_REQUEST, "Sender ung�ltig");

		// 5. Falls receiverNumber = senderNumber, dann gib einen Fehler aus
		if (receiverNumber.equals(senderNumber))
			return getResponse(Response.Status.BAD_REQUEST, "Empf�nger gleich Sender");

		AccountService accountService = new AccountService();
		Account sender;
		Account receiver;
		double tAmount = Double.valueOf(amount);
		BigDecimal bdAmount = BigDecimal.valueOf(tAmount);
		try {
			// 6. Falls Sender nicht vorhanden, dann gib einen Fehler aus
			sender = accountService.getAccount(senderNumber);
			if (sender == null)
				return getResponse(Response.Status.NOT_FOUND, "Sender nicht vorhanden");

			// 7. Falls Empf�nger nicht vorhanden, dann gib einen Fehler aus
			receiver = accountService.getAccount(receiverNumber);
			if (receiver == null)
				return getResponse(Response.Status.NOT_FOUND, "Empf�nger nicht vorhanden");

			// 8. Falls nicht genug Geld f�r �berweisung, dann gib einen Fehler aus
			// List<Transaction> transactions = sender.getTransactions();
			//
			// BigDecimal balance = new BigDecimal(0);
			// for (Transaction transaction : transactions) {
			// if (transaction.getSender().getNumber().equals(sender.getNumber()))
			// // Benutzer �berweist Geld an wen anders
			// balance = balance.subtract(transaction.getAmount());
			// else
			// // Benutzer bekommt Geld
			// balance = balance.add(transaction.getAmount());
			// }
			//
			// if (bdAmount.compareTo(balance) == 1)
			// return
			// Response.status(Response.Status.PRECONDITION_FAILED).entity("Kontostand zu
			// gering").build();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return Response.serverError().build();
		}

		Transaction transaction = new Transaction();
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setAmount(bdAmount);
		transaction.setReference(reference);
		transaction.setTransactionDate(new Date(System.currentTimeMillis()));
		TransactionService transactionService = new TransactionService();
		try {
			if (!transactionService.create(transaction))
				return getResponse(Response.Status.PRECONDITION_FAILED, "Kontostand zu gering");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build(); // Keine Ahnung ob das
			// richtig ist.
			// Wahrscheinlich muss man hier alle m�glichen Fehlerf�lle abfangen
			// und mit responsecode zur�ckgeben
			// (Siehe Schnittstelle)
		}
		executeTransactionSemaphore.release();
		return Response.ok().build();
	}

	private Response getResponse(Status status, String msg) {
		Logger logger = Logger.getLogger(getClass());
		
		logger.error( "REST: executeTransaction" + 
					 " Status: " + status.getStatusCode() + 
					 " (" + status.getReasonPhrase() + ")" +
					 " Msg: " + msg); 
		
		executeTransactionSemaphore.release();
		return Response.status(status).entity(msg).build();
 			
	}

}
