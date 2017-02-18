package nl.remco.service.klant.impl;

import java.util.List;

import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.web.KLA_GetRequest;
import nl.remco.service.klant.web.KLA_GetResponse;
import nl.remco.service.klant.web.KLA_KlantService;
import nl.remco.service.klant.web.KLA_Selectie;

import org.springframework.beans.factory.annotation.Autowired;

public class KlantServiceImpl implements KLA_KlantService {

 
	@Autowired
	private CRMCustomersDelegate crmKlantDelegate;
	@Autowired
	private KlantEnricher klantEnricher;

	@Override
	public KLA_GetResponse get(KLA_GetRequest request) {
		if (request.getSelectie()==null) {
			request.setSelectie( new KLA_Selectie());
		}

		if (request.getIds()!= null && request.getIds().isEmpty()) {
			request.setIds( null);
		}

		List<Klant> klanten= crmKlantDelegate.get( request);
		
		if (request.getSelectie().isSelectOrganisaties()) {
			klantEnricher.enrichOrganisaties(klanten);
		}
		KLA_GetResponse response= new KLA_GetResponse();
		response.setKlanten(klanten);
		return response;
	}

	public KlantEnricher getKlantEnricher() {
		return klantEnricher;
	}

	public void setKlantEnricher(KlantEnricher klantEnricher) {
		this.klantEnricher = klantEnricher;
	}



}
