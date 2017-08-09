package de.fhdw.wipbank.server.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Transaction {
    private int id;
    private Account sender;
    private Account receiver;
    private BigDecimal amount;
    private String reference;
    
    private Date transactionDate;

    @XmlTransient
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    /**
     * http://javarevisited.blogspot.de/2017/04/jaxb-date-format-example-using-annotation-XMLAdapter.html
     *
     */
    class DateTimeAdapter extends XmlAdapter<String, Date>{
        private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        @Override
        public Date unmarshal(String xml) throws Exception {
            return dateFormat.parse(xml);
        }

        @Override
        public String marshal(Date object) throws Exception {
            return dateFormat.format(object);
        }

    }

}
