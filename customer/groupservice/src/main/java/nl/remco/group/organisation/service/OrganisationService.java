package nl.remco.group.organisation.service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import nl.remco.group.service.dto.OrganisationDTO;
public interface OrganisationService {
    CompletableFuture<List<OrganisationDTO>> find( );
}
