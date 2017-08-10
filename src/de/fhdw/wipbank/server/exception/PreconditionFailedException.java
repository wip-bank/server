package de.fhdw.wipbank.server.exception;

public class PreconditionFailedException extends Exception {

    public PreconditionFailedException(String message) {
        super(message);
    }
}
