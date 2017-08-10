package de.fhdw.wipbank.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.util.Validation;

@Path("/account")
@Singleton
public class AccountResource {

    @GET
    @Path("/{number}")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAccount(@PathParam("number") String number) {
    	// Fehlerpr�fungen
    	
    	// 1. -> Format number: Die Kontonummer ist stets 4-stellig und beginnt mit einer 1 (z.B. 1005, Ausnahme: Konto der Bank: 0000)
    	if(!Validation.isAccountNumberValid(number) && !number.equals("0000"))
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Account ung�ltig").build();
    	
    	
        Account account = new Account();
        AccountService accountService = new AccountService();
        try {
            account = accountService.getAccount(number);
            if (account == null)
            	return Response.status(Response.Status.NOT_FOUND)
                        .entity("Account nicht vorhanden").build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(account).build();
    }

}
