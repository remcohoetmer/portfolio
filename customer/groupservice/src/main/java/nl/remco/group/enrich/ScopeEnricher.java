package nl.remco.group.enrich;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nl.remco.group.service.domain.Scope;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.ScopeDTO;

@Component
public class ScopeEnricher {
	private static final Logger log = LoggerFactory.getLogger(ScopeEnricher.class);

	public CompletableFuture<GroupDTO> enrichScopes( final GroupDTO groupDTO)
	{
		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
		ScopeDTO scopeDTO= groupDTO.getScope();
		CompletableFuture<GroupDTO> cf= new CompletableFuture<GroupDTO>();

		if (scopeDTO!=null && scopeDTO.getId()!= null && !scopeDTO.getId().isEmpty()) {
			String url= "http://localhost:8081/api/scope/"+scopeDTO.getId();
			try {
				asyncRestTemplate.getForEntity(url, Scope.class).addCallback(
						re -> {
							Scope scope= re.getBody();
							scopeDTO.setName(scope.getName());
							cf.complete(groupDTO);},
						error -> { 
							System.err.println("result"+ error);
							scopeDTO.setName("--Unknown--");
							cf.complete(groupDTO);});
				
			} catch (RestClientException e) {
				log.error( "HTTP failed"+ url, e);
				cf.complete(groupDTO);
			}
		} else {
			cf.complete(groupDTO);			
		}
		return cf;
	}


	public List<CompletableFuture<GroupDTO>> enrichScopes(final List<GroupDTO> groups) {
		return groups.stream().map( this::enrichScopes).collect( Collectors.toList());

	}

}
