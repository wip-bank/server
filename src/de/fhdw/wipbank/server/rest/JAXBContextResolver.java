package de.fhdw.wipbank.server.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import de.fhdw.wipbank.server.model.Account;
import de.fhdw.wipbank.server.model.Transaction;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private static final Class<?>[] CLASSES = new Class[] { Account.class, Transaction.class };
    private final JAXBContext context;

    public JAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().humanReadableFormatting(true).build(), CLASSES);
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        return context;
    }

}
