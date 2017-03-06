package nl.remco.group.crm.customers;
public interface CRMCustomersService {

	SearchCustomersRes searchCustomers(SearchCustomersReq searchUsers);

	RetrieveCustomersRes getCustomerProfile(RetrieveCustomersReq getUserProfile);

}