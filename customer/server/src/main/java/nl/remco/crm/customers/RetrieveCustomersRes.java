
package nl.remco.crm.customers;

import java.util.ArrayList;
import java.util.List;


public class RetrieveCustomersRes {

    protected List<CustomerProfile> userProfile;

    public List<CustomerProfile> getCustomerProfiles() {
        if (userProfile == null) {
            userProfile = new ArrayList<CustomerProfile>();
        }
        return this.userProfile;
    }

}
