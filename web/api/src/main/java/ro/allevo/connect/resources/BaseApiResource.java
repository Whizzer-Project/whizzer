package ro.allevo.connect.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ro.allevo.fintpws.model.BaseEntity;

public abstract class BaseApiResource<T extends BaseEntity> implements AutoCloseable {
	
	private HttpHeaders httpHeaders;
	private String url;
	private Class<?> entityClass;
	
	public BaseApiResource( String url, HttpHeaders httpHeaders, Class<?> entityClass) {
		this.httpHeaders = httpHeaders;
		this.url = url;
		this.entityClass = entityClass;
	}
	
	@SuppressWarnings("unchecked")
	protected T get(UriInfo uriInfo) {
		T entity = (T) entityClass.cast(getObject(uriInfo));
		return entity;
	}
	
	protected Object getObject(UriInfo uriInfo) {
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity entity = new HttpEntity(this.httpHeaders);
		try {
			List<PathSegment> pathSegments = uriInfo.getPathSegments();
			
			String urlSegments = "";
			for(PathSegment path:pathSegments.subList(2, pathSegments.size()))
				urlSegments += ("/"+path.getPath());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + urlSegments);
			for(String paramName : uriInfo.getQueryParameters().keySet()){
				 builder.queryParam(paramName, uriInfo.getQueryParameters().getFirst(paramName));
			}
			
			return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,  entityClass).getBody();
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
		}
		return null;
	}
	
	protected Object getObjectFromUri(URI uri) {
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity entity = new HttpEntity(this.httpHeaders);
		try {			
			return restTemplate.exchange(uri, HttpMethod.GET, entity, entityClass).getBody();
		}catch(RestClientException e) {
			System.out.println("RestClientException in getObjectFromUri = " + e.getMessage());
		}
		return null;
	}
	
}
