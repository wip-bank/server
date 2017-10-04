package de.fhdw.wipbank.server.exception;

public class PreconditionFailedException extends Exception {

    /**
     * Exception für Error 412
     *
     * @param message
     */
    public PreconditionFailedException(String message) {
        super(message);
    }
}
