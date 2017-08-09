package de.fhdw.wipbank.server.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.TransactionService;

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

		// Fehlerprüfungen
		
		// 1. -> Erlaubte Zeichen bei reference: A-Z, a-z, 0-9, Leerzeichen
		// (Pflichtfeld)
		if (!Validation.isReferenceValid(reference))
			return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Referenz ungültig").build();

		// 2. -> Format amount: 2 Nachkommastellen durch "." getrennt (z.B. 1234.99) -
		// Angaben wie 1234 statt 1234.00 sind ebenfalls erlaubt (Pflichtfeld)
		if (!Validation.isAmountValid(amount))
			return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Betrag ungültig").build();

		// 3. -> Format receiverNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(receiverNumber))
			return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Empfänger ungültig").build();

		// 4. -> Format senderNumber: Die Kontonummer ist stets 4-stellig und beginnt
		// mit einer 1 (z.B. 1005) (Pflichtfeld)
		if (!Validation.isAccountNumberValid(receiverNumber))
			return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sender ungültig").build();
		
		// 5. Falls receiverNumber = senderNumber, dann gib einen Fehler aus
		if (receiverNumber.equals(senderNumber))
			return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Empfänger gleich Sender").build();

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
			// Wahrscheinlich muss man hier alle mï¿½glichen Fehlerfï¿½lle abfangen
			// und mit responsecode zurï¿½ckgeben
			// (Siehe Schnittstelle)
		}

		return Response.ok().build();
	}

	

}
