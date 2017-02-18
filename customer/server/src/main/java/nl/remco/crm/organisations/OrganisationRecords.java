
package nl.remco.crm.organisations;

import java.util.ArrayList;
import java.util.List;

public class OrganisationRecords {

    protected List<OrganisationInfo> org;


    public List<OrganisationInfo> getOrganisation() {
        if (org == null) {
            org = new ArrayList<OrganisationInfo>();
        }
        return this.org;
    }

}
