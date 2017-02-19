
package nl.remco.crm.organisations;

public class GetOrganisationHierarchyRes {
    protected OrganisationRecords orglist;
    public OrganisationRecords getOrganisations() {
        return orglist;
    }
    public void setOrganisations(OrganisationRecords value) {
        this.orglist = value;
    }
}
