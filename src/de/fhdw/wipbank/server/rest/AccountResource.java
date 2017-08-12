package de.fhdw.wipbank.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.CreateAccount;
import de.fhdw.wipbank.server.business.FindAccountByNumber;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import org.apache.log4j.Logger;

@Path("/account")
@Singleton
public class AccountResource {

    private static final Logger logger = Logger.getLogger(AccountResource.class);

    @GET
    @Path("/{number}")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAccount(@PathParam("number") String number) {
        try {
            logger.info("Find account with number " + number);
            Account account = (new FindAccountByNumber()).findAccountByNumber(number);
            return Response.ok(account).build();
        } catch (ValidationException e) {
            logger.error(e.getMessage() + " " + number);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            logger.error(e.getMessage() + " " + number);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response createAccount(@FormParam("owner") String owner) {
        try {
            logger.info("Create account: " + owner);
            Account account = (new CreateAccount()).createAccount(owner);
            logger.info("Created account " + owner + " with number " + account.getNumber());
            return Response.ok(account).build();
        } catch (ValidationException e) {
            logger.error(e.getMessage() + " creating account");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ServerException e) {
            logger.error(e.getMessage() + " creating account");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
