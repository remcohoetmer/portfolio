
package nl.remco.crm.customers;

import java.math.BigDecimal;
public class CustomerProfileSummary {

    protected String userId;
    protected String firstname;
    protected String middlename;
    protected String lastname;
    protected String bsn;
    protected BigDecimal kvkNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String value) {
        this.firstname = value;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String value) {
        this.middlename = value;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String value) {
        this.lastname = value;
    }


    public String getBSN() {
        return bsn;
    }


    public void setBSN(String value) {
        this.bsn = value;
    }


    public BigDecimal getKvKNumber() {
        return kvkNumber;
    }

    public void setKvKNumber(BigDecimal value) {
        this.kvkNumber = value;
    }

}
