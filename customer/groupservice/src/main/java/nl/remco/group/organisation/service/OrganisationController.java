package nl.remco.group.organisation.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nl.remco.group.service.dto.OrganisationDTO;


@RestController
@RequestMapping("/api/organisation")
public final class OrganisationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    private final OrganisationService organisationService;

    @Autowired
    OrganisationController(OrganisationService service) {
        this.organisationService = service;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    CompletableFuture<List<OrganisationDTO>> findAll() {
        LOGGER.info("Finding all organisation entries");
        return organisationService.find();
    }
}
