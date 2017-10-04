package de.fhdw.wipbank.server.exception;

public class ServerException extends Exception {

    /**
     * Exception für Error 500
     *
     * @param message
     */
    public ServerException(String message) {
        super(message);
    }
}
