package nl.remco.service.common.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nl.remco.service.groep.model.Scope;



public class ScopeServiceHelper {
	private static final Logger log = LoggerFactory.getLogger(ScopeServiceHelper.class);
	RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 100;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory= new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}

	public Map<String, Scope> getScopes(Set<String> scopeIdSet) {
		Map<String, Scope> map= new HashMap<>();
		for( String id: scopeIdSet) {
			Scope scope=null;
			String url= "http://localhost:8081/api/scope/"+id;
			try {
				scope= restTemplate.getForObject(url, Scope.class);
			} catch (RestClientException e) {
				log.error( "HTTP failed"+ url, e);
				scope = new Scope( id, "--Undefined--");
			}
			map.put( scope.getId(), scope);
		}
		return map;
	}
}
