
package nl.remco.crm.customers;

import java.util.ArrayList;
import java.util.List;


public class SearchCustomersRes {

    protected List<CustomerProfile> customerProfile;

    public List<CustomerProfile> getCustomerProfiles() {
        if (customerProfile== null) {
            customerProfile= new ArrayList<CustomerProfile>();
        }
        return this.customerProfile;
    }

}
