package de.fhdw.wipbank.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

@Path("/transaction")
@Singleton
public class TransactionResource {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response transaction() {
        return Response.ok().build();
    }
}
