package nl.remco.service.groep.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import nl.remco.service.common.model.Identifiable;
import nl.remco.service.groep.model.Groep;
import nl.remco.service.groep.model.Lidmaatschap;
import nl.remco.service.klant.model.Klant;

public class StreamTest {
	@Test
	public void streamTest (){
		
		List<Groep> groepen= new ArrayList<>();
		Groep groep0= new Groep();
		
		groepen.add(groep0);
		Groep groep1= new Groep();
		groepen.add( groep1);
		
		List<Lidmaatschap> lidmaatschappen= new ArrayList<>();
		groep1.setLidmaatschappen(lidmaatschappen);
		Lidmaatschap lidm= new Lidmaatschap();
		lidm.setKlant( new Klant("123"));
		lidmaatschappen.add( lidm);

		Lidmaatschap lidm2= new Lidmaatschap();
		lidm2.setKlant( new Klant("456"));
		lidmaatschappen.add( lidm2);

		Lidmaatschap lidm3= new Lidmaatschap();
		lidm3.setKlant( new Klant());
		lidmaatschappen.add( lidm3);
		Set<String> klantIdSet= new HashSet<>();

		Groep groep2= new Groep();
		groepen.add( groep2);
		List<Lidmaatschap> lidmaatschappen2= new ArrayList<>();
		groep2.setLidmaatschappen(lidmaatschappen2);
		Lidmaatschap lidm5= new Lidmaatschap();
		lidm5.setKlant( new Klant("123"));
		lidmaatschappen2.add( lidm5);
		
		Lidmaatschap lidm4= new Lidmaatschap();
		lidm4.setKlant( new Klant("890"));
		lidmaatschappen2.add( lidm4);
		
		
		for (Groep groep: groepen) {
			if (groep.getLidmaatschappen()!= null) {
				for (Lidmaatschap lidmaatschap: groep.getLidmaatschappen()) {
					if (lidmaatschap.getKlant()!=null) {
						if (lidmaatschap.getKlant().getId()!= null) {
							klantIdSet.add( lidmaatschap.getKlant().getId());
						}
					}
				}
			}

		}
		Set<String> lidmaat= groepen.stream()
				.map(Groep::getLidmaatschappen)
				.flatMap(List::stream)
				.map(Lidmaatschap::getKlant)
				.map(Identifiable::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Assert.assertEquals( 3, klantIdSet.size());
		Assert.assertEquals( 3, lidmaat.size());
	}
}
