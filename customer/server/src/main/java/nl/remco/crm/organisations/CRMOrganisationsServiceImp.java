package nl.remco.crm.organisations;

import java.math.BigDecimal;

public class CRMOrganisationsServiceImp  {

	private OrganisationInfo bedrijfA;
	private OrganisationInfo bedrijfB;

	public CRMOrganisationsServiceImp()
	{
		bedrijfA= new OrganisationInfo();
		bedrijfA.setKvkNumber( "123456789");
		bedrijfA.setCRMNumber( new BigDecimal(8000L));
		bedrijfA.setOrganisationName( "Bedrijf A");

		bedrijfB= new OrganisationInfo();
		bedrijfB.setKvkNumber( "888888888");
		bedrijfB.setCRMNumber( new BigDecimal(8001L));
		bedrijfB.setOrganisationName( "Bedrijf B");
	}


	public SearchOrganisationRes searchOrganisations(
			SearchOrganisationReq searchSchools) {
		SearchOrganisationRes response= new SearchOrganisationRes();
		response.getOrganisations().add( bedrijfA);

		response.getOrganisations().add( bedrijfB);
		return response;
	}


	public GetOrganisationInfoResponse getOrganisationInfo(
			GetOrganisationInfoRequest request)
	{
		GetOrganisationInfoResponse response= new GetOrganisationInfoResponse();
		for (BigDecimal organisation: request.getCRMNumber()){
			if (organisation.intValue()==8000) {
				response.getOrganisations().add( bedrijfA);
			} else if  (organisation.intValue()==8001) {
				response.getOrganisations().add( bedrijfB);
			}
		}
		return response;
	}



}
