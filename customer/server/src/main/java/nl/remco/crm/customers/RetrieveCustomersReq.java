
package nl.remco.crm.customers;

import java.util.ArrayList;
import java.util.List;


public class RetrieveCustomersReq {

    protected List<String> userId;

    public List<String> getUserId() {
        if (userId == null) {
            userId = new ArrayList<String>();
        }
        return this.userId;
    }

}
