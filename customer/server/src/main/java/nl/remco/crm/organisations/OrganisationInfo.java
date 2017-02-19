
package nl.remco.crm.organisations;

import java.math.BigDecimal;


public class OrganisationInfo {

    protected BigDecimal crmNumber;
    protected String kvkNumber;
    protected String organisationName;
    protected String locationName;
    protected String alias;
    protected String street;
    protected String houseNumber;
    protected String postalCode;
    protected String city;
    protected String id;

    public BigDecimal getCRMNumber() {
        return crmNumber;
    }


    public void setCRMNumber(BigDecimal value) {
        this.crmNumber = value;
    }

    public String getKvkNumber() {
        return kvkNumber;
    }

    public void setKvkNumber(String value) {
        this.kvkNumber = value;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    public String getLocationName() {
        return locationName;
    }


    public void setLocationName(String value) {
        this.locationName = value;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String value) {
        this.street = value;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String value) {
        this.houseNumber = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

}
