package nl.remco.service.organisatie.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import nl.remco.group.crm.organisations.CRMOrganisationsServiceImp;
import nl.remco.group.crm.organisations.GetOrganisationInfoRequest;
import nl.remco.group.crm.organisations.GetOrganisationInfoResponse;
import nl.remco.group.crm.organisations.OrganisationInfo;
import nl.remco.group.crm.organisations.SearchOrganisationReq;
import nl.remco.group.crm.organisations.SearchOrganisationRes;
import nl.remco.service.common.model.LifeCycleBeheer.Status;
import nl.remco.service.organisatie.model.Organisatie;
import nl.remco.service.organisatie.web.ORG_GetRequest;


public class CRMOrganisationsDelegate
{
	@Autowired
	private	CRMOrganisationsServiceImp organisationServices;

	public List<Organisatie> getOrganisaties(ORG_GetRequest request)  {

			List<Organisatie> organisaties= new ArrayList<Organisatie>();
			if (request.getIds()!= null) {
				// query op basis van unieke ID
				GetOrganisationInfoRequest getOrganisationInfo= new GetOrganisationInfoRequest();
				for (String id: request.getIds()) {
					getOrganisationInfo.getCRMNumber().add( new BigDecimal( id));
				}

				GetOrganisationInfoResponse result= organisationServices.getOrganisationInfo( getOrganisationInfo);
				for (OrganisationInfo school: result.getOrganisations()) {
					convert( organisaties, school);
				}
			} else {
				// doe een zoek
				SearchOrganisationReq searchOrganisationsRequest= new SearchOrganisationReq();

				searchOrganisationsRequest.setPattern( request.getFilter().getNaam());

				SearchOrganisationRes result= organisationServices.searchOrganisations( searchOrganisationsRequest);
				for (OrganisationInfo org: result.getOrganisations()) {
					convert( organisaties, org);
				}
			}
			return organisaties;

	}
	
	public Map<String, Organisatie> getOrganisaties(Set<String> organisatieIdSet) {
		ORG_GetRequest request= new ORG_GetRequest();
		request.setIds( new ArrayList<String>(organisatieIdSet));
		List<Organisatie> organisaties= getOrganisaties( request);

		// zet de organisaties in een map om ze snel te vinden
		Map<String, Organisatie> returnedOrganisatieMap= new HashMap<String, Organisatie>();
		for( Organisatie organisatie: organisaties) {
			returnedOrganisatieMap.put(organisatie.getId(), organisatie);
		}
		return returnedOrganisatieMap;
	}
	

	private void convert(List<Organisatie> organisaties, OrganisationInfo org) {
		Organisatie organisatie= new Organisatie();
		organisatie.setId( org.getCRMNumber().toString());
		organisatie.setNaam(org.getOrganisationName());
		organisatie.setKvkNummer( org.getKvkNumber());
		organisatie.setStatus( Status.Actief);
		organisatie.setStraatnaam( "Street");
		organisatie.setNummer("1");
		organisatie.setPostcode("1234AB");
		organisatie.setPlaatsnaam("Town");
		organisaties.add( organisatie);
	}

	public CRMOrganisationsServiceImp getSchoolServices() {
		return organisationServices;
	}
	

	public void setSchoolServices(CRMOrganisationsServiceImp service) {
		this.organisationServices = service;
	}
}
