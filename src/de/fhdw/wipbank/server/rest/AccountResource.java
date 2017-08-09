package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.service.AccountService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
@Singleton
public class AccountResource {

	private static final int CODE_BAD_REQUEST = 400;
	private static final int CODE_NOT_FOUND = 404;
	private static final int CODE_INTERNAL_SERVER_ERROR = 500;
	
    @GET
    @Path("/{number}")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAccount(@PathParam("number") String number) {
    	// Fehlerprüfungen
    	
    	// 1. -> Format number: Die Kontonummer ist stets 4-stellig und beginnt mit einer 1 (z.B. 1005, Ausnahme: Konto der Bank: 0000)
    	if(!Validation.isAccountNumberValid(number) && !number.equals("0000"))
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Account ungültig").build();
    	
    	
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

}
