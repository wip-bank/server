package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;
import de.fhdw.wipbank.server.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transaction")
@Singleton
public class TransactionResource {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response transaction() {
        return Response.ok().build();
    }
}
