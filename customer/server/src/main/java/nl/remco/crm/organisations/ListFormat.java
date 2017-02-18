
package nl.remco.crm.organisations;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ListFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Flat"/>
 *     &lt;enumeration value="Hierarchical"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ListFormat")
@XmlEnum
public enum ListFormat {

    @XmlEnumValue("Flat")
    FLAT("Flat"),
    @XmlEnumValue("Hierarchical")
    HIERARCHICAL("Hierarchical");
    private final String value;

    ListFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ListFormat fromValue(String v) {
        for (ListFormat c: ListFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
