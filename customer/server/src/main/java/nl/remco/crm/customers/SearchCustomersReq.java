
package nl.remco.crm.customers;

public class SearchCustomersReq {
    protected CustomerSearchProfile userProfile;
    public CustomerSearchProfile getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(CustomerSearchProfile value) {
        this.userProfile = value;
    }

}
