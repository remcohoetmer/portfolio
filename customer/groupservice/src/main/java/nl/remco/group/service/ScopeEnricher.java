package nl.remco.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nl.remco.group.service.domain.Scope;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.ScopeDTO;

@Component
public class ScopeEnricher {
	private static final Logger log = LoggerFactory.getLogger(ScopeEnricher.class);

	RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 100;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory= new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}
	
	public CompletableFuture<GroupDTO> enrichScopes( final GroupDTO groupDTO)
	{
		ScopeDTO scopeDTO= groupDTO.getScope();
		if (scopeDTO!=null && scopeDTO.getId()!= null && !scopeDTO.getId().isEmpty()) {
			String url= "http://localhost:8081/api/scope/"+scopeDTO.getId();
			try {
				Scope scope= restTemplate.getForObject(url, Scope.class);
				scopeDTO.setName(scope.getName());
			} catch (RestClientException e) {
				log.error( "HTTP failed"+ url, e);
			}
		}
		return CompletableFuture.completedFuture(groupDTO);
	}


	public CompletableFuture<List<GroupDTO>> enrichScopes(final List<GroupDTO> groups) {
		List<CompletableFuture<GroupDTO>> list= new ArrayList<>();
		for (GroupDTO group: groups) {
			list.add( enrichScopes(group));
		}
		return CompletableFuture.allOf(list.stream().toArray(CompletableFuture[]::new))
				.thenApply(dummy->{ return groups;});
	}

}
