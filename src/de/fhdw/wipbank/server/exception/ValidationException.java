package de.fhdw.wipbank.server.exception;

/**
 * @author Philipp Dyck
 */
public class ValidationException extends Exception {

    /**
     * Exception für Error 400
     *
     * @param message
     */
    public ValidationException(String message) {
        super(message);
    }
}
