package de.fhdw.wipbank.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * http://javarevisited.blogspot.de/2017/04/jaxb-date-format-example-using-annotation-XMLAdapter.html
 *
 */
public class DateTimeAdapter extends XmlAdapter<String, Date>{
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * Ein Datum parsen
     *
     * @param xml
     * @return
     * @throws Exception
     */
    @Override
    public Date unmarshal(String xml) throws Exception {
        return dateFormat.parse(xml);
    }

    /**
     * Ein Datum in einen String umwandeln
     *
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public String marshal(Date object) throws Exception {
        return dateFormat.format(object);
    }

}
