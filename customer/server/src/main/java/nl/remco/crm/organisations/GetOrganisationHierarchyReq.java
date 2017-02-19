
package nl.remco.crm.organisations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetOrganisationHierarchyReq {

	protected GetOrganisationHierarchyReq.Organisation orgs;

	public GetOrganisationHierarchyReq.Organisation getOrganisations() {
		return orgs;
	}

	public static class Organisation {
		protected List<BigDecimal> crmNumber;
		public List<BigDecimal> getCRMNumber() {
			if (crmNumber == null) {
				crmNumber = new ArrayList<BigDecimal>();
			}
			return this.crmNumber;
		}
	}
}
