package de.fhdw.wipbank.server.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class TransactionList {

    private List<Transaction> list;

    @XmlElement(name = "transactions")
    public List<Transaction> getList() {
        return list;
    }

    public void setList(List<Transaction> list) {
        this.list = list;
    }
}
