
package nl.remco.crm.customers;

import java.util.ArrayList;
import java.util.List;


public class SearchCustomersRes {

    protected List<CustomerProfileSummary> customerProfileSummary;

    public List<CustomerProfileSummary> getCustomerProfileSummary() {
        if (customerProfileSummary == null) {
            customerProfileSummary = new ArrayList<CustomerProfileSummary>();
        }
        return this.customerProfileSummary;
    }

}
