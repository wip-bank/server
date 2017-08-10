package de.fhdw.wipbank.server.rest;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

import org.apache.log4j.Logger;


@Path("/transaction")
@Singleton
public class TransactionResource {

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
		Logger logger = Logger.getLogger(getClass());
		
		logger.info( "REST: executeTransaction" + 
					 " senderNumber: " + senderNumber + 
					 " receiverNumber: " + receiverNumber + 
					 " amount: " + amount + 
					 " reference: " + reference); 

		// Fehlerprüfungen

		// 1. -> Erlaubte Zeichen bei reference: A-Z, a-z, 0-9, Leerzeichen
		// (Pflichtfeld)
		if (!Validation.isReferenceValid(reference))
			return Response.status(Response.Status.BAD_REQUEST).entity("Referenz ungültig").build();

		// 2. -> Format amount: 2 Nachkommastellen durch "." getrennt (z.B. 1234.99) -
		// Angaben wie 1234 statt 1234.00 sind ebenfalls erlaubt (Pflichtfeld)
		if (!Validation.isAmountValid(amount))
			return Response.status(Response.Status.BAD_REQUEST).entity("Betrag ungültig").build();

		// 3. -> Format receiverNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(receiverNumber))
			return Response.status(Response.Status.BAD_REQUEST).entity("Empfänger ungültig").build();

		// 4. -> Format senderNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(senderNumber))
			return Response.status(Response.Status.BAD_REQUEST).entity("Sender ungültig").build();

		// 5. Falls receiverNumber = senderNumber, dann gib einen Fehler aus
		if (receiverNumber.equals(senderNumber))
			return Response.status(Response.Status.BAD_REQUEST).entity("Empfänger gleich Sender").build();

		AccountService accountService = new AccountService();
		Account sender;
		Account receiver;
		double tAmount = Double.valueOf(amount);
		BigDecimal bdAmount = BigDecimal.valueOf(tAmount);
		try {
			// 6. Falls Sender nicht vorhanden, dann gib einen Fehler aus
			sender = accountService.getAccount(senderNumber);
			if (sender == null)
				return Response.status(Response.Status.NOT_FOUND).entity("Sender nicht vorhanden").build();

			// 7. Falls Empfänger nicht vorhanden, dann gib einen Fehler aus
			receiver = accountService.getAccount(receiverNumber);
			if (receiver == null)
				return Response.status(Response.Status.NOT_FOUND).entity("Empfänger nicht vorhanden").build();

			// 8. Falls nicht genug Geld für Überweisung, dann gib einen Fehler aus
			// List<Transaction> transactions = sender.getTransactions();
			//
			// BigDecimal balance = new BigDecimal(0);
			// for (Transaction transaction : transactions) {
			// if (transaction.getSender().getNumber().equals(sender.getNumber()))
			// // Benutzer überweist Geld an wen anders
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
				return Response.status(Response.Status.PRECONDITION_FAILED).entity("Kontostand zu gering").build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build(); // Keine Ahnung ob das
			// richtig ist.
			// Wahrscheinlich muss man hier alle mï¿½glichen Fehlerfï¿½lle abfangen
			// und mit responsecode zurï¿½ckgeben
			// (Siehe Schnittstelle)
		}

		return Response.ok().build();
	}

	

}
