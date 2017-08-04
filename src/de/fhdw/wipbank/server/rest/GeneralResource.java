package de.fhdw.wipbank.server.rest;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;
import de.fhdw.wipbank.server.service.AccountService;
import de.fhdw.wipbank.server.service.TransactionService;

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
