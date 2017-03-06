
package nl.remco.group.crm.customers;

import java.math.BigDecimal;
import java.util.Date;



public class CustomerProfile {

    protected String userId;
    protected String firstname;
    protected String middlename;
    protected String lastname;
    protected Date dateOfBirth;
    protected Gender gender;
    protected String eMailAddress;
    protected String bsn;

    protected BigDecimal organisationNumber;

    protected String role;

  
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date value) {
        this.dateOfBirth = value;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender value) {
        this.gender = value;
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

    public BigDecimal getOrganisationNumber() {
        return organisationNumber;
    }

    public void setOrganisationNumber(BigDecimal value) {
        this.organisationNumber = value;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String value) {
        this.role = value;
    }


}
