package de.fhdw.wipbank.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.CreateAccount;
import de.fhdw.wipbank.server.business.FindAccountByNumber;
import de.fhdw.wipbank.server.business.GetAllAccounts;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.AccountList;
import org.apache.log4j.Logger;

import java.util.List;

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
            return ResponseBuilder.ok(account);
        } catch (ValidationException e) {
            logger.error(e.getMessage() + " " + number);
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            logger.error(e.getMessage() + " " + number);
            return ResponseBuilder.notFound(e.getMessage());
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
            return ResponseBuilder.ok(account);
        } catch (ValidationException e) {
            logger.error(e.getMessage() + " creating account");
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (ServerException e) {
            logger.error(e.getMessage() + " creating account");
            return ResponseBuilder.error(e.getMessage());
        }
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAll() {
        try {
            logger.info("GET /account");
            List<Account> accounts = (new GetAllAccounts()).getAll();
            return ResponseBuilder.ok(wrapAccounts(accounts));
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.error(e.getMessage());
        }
    }

    private AccountList wrapAccounts(List<Account> accounts) {
        AccountList list = new AccountList();
        list.setList(accounts);
        return list;
    }


}
