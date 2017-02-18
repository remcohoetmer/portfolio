
package nl.remco.crm.customers;

import java.util.ArrayList;
import java.util.List;


public class SearchCustomersRes {

    protected List<CustomerProfileSummary> userProfileSummary;

    public List<CustomerProfileSummary> getUserProfileLite() {
        if (userProfileSummary == null) {
            userProfileSummary = new ArrayList<CustomerProfileSummary>();
        }
        return this.userProfileSummary;
    }

}
