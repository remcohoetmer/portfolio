
package nl.remco.group.crm.organisations;

import java.util.ArrayList;
import java.util.List;

public class GetOrganisationInfoResponse {

    protected List<OrganisationInfo> organisations;


    public List<OrganisationInfo> getOrganisations() {
        if (organisations == null) {
            organisations = new ArrayList<OrganisationInfo>();
        }
        return this.organisations;
    }

}
