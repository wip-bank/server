package de.fhdw.wipbank.server.service;

/**
 * Created by pdyck on 20.07.17.
 */
public interface Service<T> {
    void createTable() throws Exception;
    void create(T object);
    T findById(T object);
    T update(T object);
}
