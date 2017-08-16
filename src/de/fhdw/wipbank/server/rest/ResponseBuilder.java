package de.fhdw.wipbank.server.rest;

import de.fhdw.wipbank.server.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class ResponseBuilder {

    public static Response ok(Object object) {
        return buildResponse(Response.Status.OK, object);
    }

    public static Response notFound(String message) {
        return buildResponse(Response.Status.NOT_FOUND, buildError(message));
    }

    public static Response badRequest(String message) {
        return buildResponse(Response.Status.BAD_REQUEST, buildError(message));
    }

    public static Response preconditionFailed(String message) {
        return buildResponse(Response.Status.PRECONDITION_FAILED, buildError(message));
    }

    public static Response error(String message) {
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, buildError(message));
    }

    private static Response buildResponse(Response.Status status, Object object) {
        return Response.status(status).entity(object)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    private static ErrorResponse buildError(String message) {
        ErrorResponse error = new ErrorResponse();
        error.setError(message);
        return error;
    }
}
