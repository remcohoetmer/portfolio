
package nl.remco.crm.customers;

import java.math.BigDecimal;
import java.util.Date;


public class CustomerSearchProfile {

    protected String firstname;
    protected String lastname;

    protected Date dateOfBirth;
    protected String eMailAddress;
    protected String bsn;
    protected BigDecimal kvkNumber;


    public void setFirstname(String value) {
        this.firstname = value;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String value) {
        this.lastname = value;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date value) {
        this.dateOfBirth = value;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }


    public void setEMailAddress(String value) {
        this.eMailAddress = value;
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
