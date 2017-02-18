package nl.remco.crm.customers;
public interface CRMCustomersService {

	SearchCustomersRes searchUsers(SearchCustomersReq searchUsers);

	RetrieveCustomersRes getUserProfile(RetrieveCustomersReq getUserProfile);

}