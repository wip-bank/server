package de.fhdw.wipbank.server.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Philipp Dyck
 */
@XmlRootElement
public class ErrorResponse {

    private String error;

    /**
     * Gibt die Fehlermeldung zurück.
     *
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * Setzt die Fehlermeldung.
     *
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }
}
