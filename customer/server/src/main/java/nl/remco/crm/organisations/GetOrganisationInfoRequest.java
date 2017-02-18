
package nl.remco.crm.organisations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetOrganisationInfoRequest {

    protected List<BigDecimal> kvkNumber;
    public List<BigDecimal> getKvKNumber() {
        if (kvkNumber == null) {
            kvkNumber = new ArrayList<BigDecimal>();
        }
        return this.kvkNumber;
    }


}
