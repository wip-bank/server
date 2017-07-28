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


    @GET
    @Path("/{number}")
    @Produces({MediaType.APPLICATION_JSON + "; charset=utf-8" })
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

}
