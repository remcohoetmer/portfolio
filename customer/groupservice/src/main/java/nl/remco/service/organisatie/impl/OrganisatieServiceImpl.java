package nl.remco.service.organisatie.impl;

import java.util.List;

import nl.remco.service.common.web.BadRequestException;
import nl.remco.service.organisatie.model.Organisatie;
import nl.remco.service.organisatie.web.ORG_GetRequest;
import nl.remco.service.organisatie.web.ORG_GetResponse;
import nl.remco.service.organisatie.web.ORG_OrganisatieService;
import nl.remco.service.organisatie.web.ORG_Selectie;

import org.springframework.beans.factory.annotation.Autowired;

public class OrganisatieServiceImpl implements ORG_OrganisatieService {

	private CRMOrganisationsDelegate organisatieDelegate;

	public CRMOrganisationsDelegate getOrganisatieDelegate() {
		return organisatieDelegate;
	}

	@Autowired
	public void setOrganisatieDelegate(CRMOrganisationsDelegate organisatieDelegate) {
		this.organisatieDelegate = organisatieDelegate;
	}


	public ORG_GetResponse get( ORG_GetRequest request)
	{
		if (request.getSelectie() == null) {
			request.setSelectie( new ORG_Selectie()); 
		}
		if ( request.getIds()!= null && request.getIds().isEmpty()) {
			throw new BadRequestException( "Lijst met IDs leeg");
		}
		
		List<Organisatie> organisaties= organisatieDelegate.getOrganisaties( request);

		ORG_GetResponse response= new ORG_GetResponse();
		response.setOrganisaties( organisaties);
		return response;
	}
}
