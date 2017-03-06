package nl.remco.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.OrganisationDTO;
import nl.remco.group.service.dto.PersonDTO;
import nl.remco.group.service.dto.ScopeDTO;


@RestController
@RequestMapping("/api/group")
public final class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    @Autowired
    GroupController(GroupService service) {
        this.groupService = service;
    }
    GroupDTO get() {
		GroupDTO group= new GroupDTO();
		group.setName( "Groepie");
		group.setDescription( "Beschrijving");
		group.setPeriod("2017");

		group.setProduct( "Fiets");
		group.setCode( "Code2");
		OrganisationDTO organisation=  new OrganisationDTO();
		organisation.setName( "Company");
		group.setOrganisation(organisation);
		ScopeDTO scope= new ScopeDTO();
		group.setName("Marketing");
		
		group.setScope(scope);


		List<String> features= new ArrayList<String>();
		features.add( "KK1");
		features.add( "KK2");
		group.setFeatures(features);

		List<MembershipDTO> lidmaatschappen= new ArrayList<>();
		MembershipDTO lidmaatschap= new MembershipDTO();
		PersonDTO persoon= new PersonDTO("person1");
		lidmaatschap.setPersoon( persoon);
		lidmaatschap.setRol( "member");
		lidmaatschappen.add( lidmaatschap);
		group.setMemberships(lidmaatschappen);

		return group;

	}
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:63062")
    GroupDTO create(@RequestBody @Valid GroupDTO groupEntry) {
        LOGGER.info("Creating a new group entry with information: {}", groupEntry);
    
        groupEntry= get();

        GroupDTO created = groupService.create(groupEntry);
        LOGGER.info("Created a new group entry with information: {}", created);

        return created;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "*")
    GroupDTO delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting group entry with id: {}", id);

        GroupDTO deleted = groupService.delete(id);
        LOGGER.info("Deleted group entry with information: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    @Async
    CompletableFuture<List<GroupDTO>> findAll() {
        LOGGER.info("Finding all group entries");
        CompletableFuture<List<GroupDTO>> groupEntries = groupService.findAll();
//        LOGGER.info("Found {} group entries", groupEntries.size());

        return groupEntries;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:63062")
    GroupDTO findById(@PathVariable("id") String id) {
        LOGGER.info("Finding group entry with id: {}", id);

        GroupDTO groupEntry = groupService.findById(id);
        LOGGER.info("Found group entry with information: {}", groupEntry);

        return groupEntry;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "*")
    GroupDTO update(@RequestBody @Valid GroupDTO groupEntry, @PathVariable("id") String id) {
        LOGGER.info("Updating group entry with information: {}", groupEntry);
        groupEntry.setId(id);

        GroupDTO updated = groupService.update(groupEntry);
        LOGGER.info("Updated group entry with information: {}", updated);

        return updated;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @CrossOrigin(origins = "http://localhost:63062")
    public void handlegroupNotFound(GroupNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }
}
