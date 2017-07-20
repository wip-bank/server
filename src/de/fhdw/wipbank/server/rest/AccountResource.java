package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;
import de.fhdw.wipbank.server.model.Account;

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
    public Response getAccount(@PathParam("number") int number) {
        Account account = new Account();
        account.setId(number);
        return Response.ok(account).build();
    }

}
