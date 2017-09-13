package de.fhdw.wipbank.server.service;

import java.sql.SQLException;
import java.util.List;

public interface Service<T> {

    void createTable() throws SQLException;
    boolean create(T object) throws SQLException;
    List<T> getAll() throws SQLException;

}
