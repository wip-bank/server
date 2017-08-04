package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.TransactionService;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Date;

@Path("/transaction")
@Singleton
public class TransactionResource {

    @POST
    @Path("/")
    /**
     * https://stackoverflow.com/a/8194612
     * @param senderNumber
     * @param receiverNumber
     * @param amount
     * @param reference
     * @return
     */
    public Response executeTransaction(@FormParam("senderNumber") String senderNumber,
                                       @FormParam("receiverNumber") String receiverNumber,
                                       @FormParam("amount") String amount,
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
            // Wahrscheinlich muss man hier alle m�glichen Fehlerf�lle abfangen
            // und mit responsecode zur�ckgeben
            // (Siehe Schnittstelle)
        }
        return Response.ok().build();
    }

}
