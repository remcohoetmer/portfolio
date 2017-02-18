
package nl.remco.crm.organisations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Schools" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ASSUNumber" type="{http://licentiesonline.thiememeulenhoff.nl/SchoolServices}ASSUNumber" maxOccurs="unbounded"/>
 *                   &lt;element name="BrinCode" type="{http://licentiesonline.thiememeulenhoff.nl/SchoolServices}BrinCode" maxOccurs="unbounded"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ListFormat" type="{http://licentiesonline.thiememeulenhoff.nl/SchoolServices}ListFormat" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "schools",
    "listFormat"
})
@XmlRootElement(name = "getSchoolTreeRequest")
public class GetOrganisationHierarchyReq {

    @XmlElement(name = "Schools")
    protected GetOrganisationHierarchyReq.Schools schools;
    @XmlElement(name = "ListFormat")
    protected ListFormat listFormat;

    /**
     * Gets the value of the schools property.
     * 
     * @return
     *     possible object is
     *     {@link GetOrganisationHierarchyReq.Schools }
     *     
     */
    public GetOrganisationHierarchyReq.Schools getSchools() {
        return schools;
    }

    /**
     * Sets the value of the schools property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetOrganisationHierarchyReq.Schools }
     *     
     */
    public void setSchools(GetOrganisationHierarchyReq.Schools value) {
        this.schools = value;
    }

    /**
     * Gets the value of the listFormat property.
     * 
     * @return
     *     possible object is
     *     {@link ListFormat }
     *     
     */
    public ListFormat getListFormat() {
        return listFormat;
    }

    /**
     * Sets the value of the listFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListFormat }
     *     
     */
    public void setListFormat(ListFormat value) {
        this.listFormat = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="ASSUNumber" type="{http://licentiesonline.thiememeulenhoff.nl/SchoolServices}ASSUNumber" maxOccurs="unbounded"/>
     *         &lt;element name="BrinCode" type="{http://licentiesonline.thiememeulenhoff.nl/SchoolServices}BrinCode" maxOccurs="unbounded"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "assuNumber",
        "brinCode"
    })
    public static class Schools {

        @XmlElement(name = "ASSUNumber")
        protected List<BigDecimal> assuNumber;
        @XmlElement(name = "BrinCode")
        protected List<String> brinCode;

        /**
         * Gets the value of the assuNumber property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the assuNumber property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getASSUNumber().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BigDecimal }
         * 
         * 
         */
        public List<BigDecimal> getASSUNumber() {
            if (assuNumber == null) {
                assuNumber = new ArrayList<BigDecimal>();
            }
            return this.assuNumber;
        }

        /**
         * Gets the value of the brinCode property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the brinCode property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBrinCode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getBrinCode() {
            if (brinCode == null) {
                brinCode = new ArrayList<String>();
            }
            return this.brinCode;
        }

    }

}
