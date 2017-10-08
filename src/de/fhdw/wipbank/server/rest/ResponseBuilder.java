package de.fhdw.wipbank.server.rest;

import javax.ws.rs.core.Response;

/**
 * @author Philipp Dyck
 */
public class ResponseBuilder {

    /**
     * Baut eine Response mit Status 200
     * @param object
     * @return
     */
    public static Response ok(Object object) {
        return buildResponse(Response.Status.OK, object);
    }

    /**
     * Baut eine Reponse mit Status 404
     * @param message
     * @return
     */
    public static Response notFound(String message) {
        return buildResponse(Response.Status.NOT_FOUND, message);
    }

    /**
     * Baut eine Response mit Status 400
     * @param message
     * @return
     */
    public static Response badRequest(String message) {
        return buildResponse(Response.Status.BAD_REQUEST, message);
    }

    /**
     * Baut eine Response mit Status 412
     * @param message
     * @return
     */
    public static Response preconditionFailed(String message) {
        return buildResponse(Response.Status.PRECONDITION_FAILED, message);
    }

    /**
     * Baut eine Reponse mit Status 500
     *
     * @param message
     * @return
     */
    public static Response error(String message) {
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Baut eine Response mit Standard Headern
     *
     * @param status
     * @param object
     * @return
     */
    private static Response buildResponse(Response.Status status, Object object) {
        return Response.status(status).entity(object)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
}
