package de.fhdw.wipbank.server.exception;

/**
 * @author Philipp Dyck
 */
public class NotFoundException extends Exception {

    /**
     * Exception für Error 404
     *
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
