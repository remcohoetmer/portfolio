package nl.remco.service.groep.web;

import nl.remco.service.common.model.Identifiable;

public class GRP_CreateResponse extends Identifiable {
		private String groepscode;

		public String getGroepscode() {
			return groepscode;
		}

		public void setGroepscode(String groepscode) {
			this.groepscode = groepscode;
		}
		
}
