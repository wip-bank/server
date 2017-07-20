package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Singleton
public class GeneralResource {

    @GET
    @Path("/health")
    @Produces({ MediaType.TEXT_PLAIN + "; charset=utf-8" })
    public Response health() {
        return Response.ok("ok").build();
    }

}
