package de.fhdw.wipbank.server.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Philipp Dyck
 */
@XmlRootElement
public class AccountList {

    private List<Account> list;

    @XmlElement(name = "accounts")
    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }
}
