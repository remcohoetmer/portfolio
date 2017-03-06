
package nl.remco.group.crm.organisations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetOrganisationInfoRequest {

    protected List<BigDecimal> crmNumber;
    public List<BigDecimal> getCRMNumber() {
        if (crmNumber == null) {
            crmNumber = new ArrayList<BigDecimal>();
        }
        return this.crmNumber;
    }


}
