package de.fhdw.wipbank.server.service;

import java.util.List;

public interface Service<T> {
    void createTable() throws Exception;
    void create(T object) throws Exception;
    List<T> getAll() throws Exception; 
//    T findById(T object);
//    T update(T object);
}
