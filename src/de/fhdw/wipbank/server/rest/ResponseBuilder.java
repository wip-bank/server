package de.fhdw.wipbank.server.rest;

import javax.ws.rs.core.Response;

public class ResponseBuilder {

    public static Response ok(Object object) {
        return buildResponse(Response.Status.OK, object);
    }

    public static Response notFound(Object object) {
        return buildResponse(Response.Status.NOT_FOUND, object);
    }

    public static Response badRequest(Object object) {
        return buildResponse(Response.Status.BAD_REQUEST, object);
    }

    public static Response error(Object object) {
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, object);
    }

    private static Response buildResponse(Response.Status status, Object object) {
        return Response.status(status).entity(object)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
}
