package de.fhdw.wipbank.server.service;

import java.util.List;

public interface Service<T> {

    void createTable() throws Exception;
    boolean create(T object) throws Exception;
    List<T> getAll() throws Exception;

}
