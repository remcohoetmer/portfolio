package nl.remco.service.groep.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nl.remco.service.klant.model.Klant;
import nl.remco.service.klant.web.KLA_GetRequest;
import nl.remco.service.klant.web.KLA_GetResponse;
import nl.remco.service.klant.web.KLA_KlantService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml"})	
public class KlantenTest {
	
	@Autowired
	private KLA_KlantService klantService;

	@Test
	public void test() {
		KLA_GetRequest request= new KLA_GetRequest();
	
		KLA_GetResponse response= klantService.get(request);
		List<Klant> klanten= response.getKlanten();
	}

}
