package nl.remco.service.klant.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nl.remco.service.common.model.LifeCycleBeheer;
import nl.remco.service.jsonutils.CustomJsonDateDeserializer;

public class Klant extends LifeCycleBeheer {

	private String voornaam;
	private String voorletters;
	private String voorvoegselAchternaam;
	private String achternaam;
	private String emailAdres;
	private Date geboortedatum;
	private Geslacht geslacht;
	private List <Inschrijving> inschrijvingen;
	
	protected Status status;

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public List<Inschrijving> getInschrijvingen() {
		return inschrijvingen;
	}
	public void setInschrijvingen(List<Inschrijving> inschrijvingen) {
		this.inschrijvingen = inschrijvingen;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getVoorletters() {
		return voorletters;
	}
	public void setVoorletters(String voorletters) {
		this.voorletters = voorletters;
	}
	public Date getGeboortedatum() {
		return geboortedatum;
	}
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	public String getVoorvoegselAchternaam() {
		return voorvoegselAchternaam;
	}
	public void setVoorvoegselAchternaam(String voorvoegselAchternaam) {
		this.voorvoegselAchternaam = voorvoegselAchternaam;
	}
	public String getAchternaam() {
		return achternaam;
	}
	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}
	public String getEmailAdres() {
		return emailAdres;
	}
	public void setEmailAdres(String emailAdres) {
		this.emailAdres = emailAdres;
	}
	public Geslacht getGeslacht() {
		return geslacht;
	}
	public void setGeslacht(Geslacht geslacht) {
		this.geslacht = geslacht;
	}
	public Klant()
	{
		inschrijvingen= new ArrayList<Inschrijving>();
	}
	
	public Klant(String id) {
		super( id);
		inschrijvingen= new ArrayList<Inschrijving>();
	}
	@Override
	public String toString() {
		String s="Gebruiker[id=" + id + ", status=" + status+ ", emailAdres=" + emailAdres
				+ ", voornaam=" + voornaam+ ", achternaam=" + achternaam
				+ ", geboortedatum=" + geboortedatum;
		if (inschrijvingen != null) {
			for (Inschrijving in : inschrijvingen) {
				s+= "\n  {" + in.toString() + "}";
			}
		}
		return s+ "]";
	}
	public String shortString() {
		StringBuffer s= new StringBuffer();
		s.append( "Gebruiker ");
		if (voornaam!=null) {
			s.append( voornaam + " ");
		}
		if (voorvoegselAchternaam !=null) {
			s.append( voorvoegselAchternaam + " ");
		}
		if (achternaam !=null) {
			s.append( achternaam+ " ");
		}
		if (emailAdres !=null) {
			s.append( "[" + emailAdres+ "]");
		}
		return s.toString();
	}
}
