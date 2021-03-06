package de.fhdw.wipbank.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.business.CreateAccount;
import de.fhdw.wipbank.server.business.FindAccountByNumber;
import de.fhdw.wipbank.server.business.GetAllAccounts;
import de.fhdw.wipbank.server.business.UpdateAccount;
import de.fhdw.wipbank.server.exception.NotFoundException;
import de.fhdw.wipbank.server.exception.ServerException;
import de.fhdw.wipbank.server.exception.ValidationException;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.AccountList;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Philipp Dyck
 *
 */
@Path("/account")
@Singleton
public class AccountResource {

    private static final Logger logger = Logger.getLogger(AccountResource.class);

    /**
     * Sucht nach dem Account mit der angegebenen Kontonummer
     *
     * @param number
     * @return
     */
    @GET
    @Path("/{number}")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response getAccount(@PathParam("number") String number) {
        try {
            logger.info("GET /account/" + number);
            Account account = (new FindAccountByNumber()).findAccountByNumber(number);
            return ResponseBuilder.ok(account);
        } catch (ValidationException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            logger.error("/account/" + number + " not found");
            return ResponseBuilder.notFound(e.getMessage());
        }
    }

    /**
     * Erstellt einen neuen Account
     *
     * @param owner
     * @return
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    public Response createAccount(@FormParam("owner") String owner) {
        try {
            logger.info("POST /account [owner: '" + owner + "']");
            Account account = (new CreateAccount()).createAccount(owner);
            return ResponseBuilder.ok(account);
        } catch (ValidationException e) {
            logger.error("Invalid data '" + owner + "'");
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.error(e.getMessage());
        }
    }

    /**
     * Gibt eine Liste aller Accounts zurück
     *
     * @return
     */
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

    /**
     * Ändert den Nutzernamen eines Accounts
     *
     * @param number
     * @param owner
     * @return
     */
    @PUT
    @Path("/{number}")
    @Produces({ MediaType.APPLICATION_JSON + "; charset=utf-8" })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateAccount(
            @PathParam("number") String number,
            @FormParam("owner") String owner) {
        try {
            logger.info("PUT /account/" + number + " [owner: '" + owner + "']");
            Account account = (new UpdateAccount()).updateAccount(number, owner);
            return ResponseBuilder.ok(account);
        } catch (ValidationException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseBuilder.notFound(e.getMessage());
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
