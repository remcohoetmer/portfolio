
package nl.remco.crm.customers;

public enum Gender {

    MALE("Male"),
    FEMALE("Female");
    private final String value;

    Gender(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
