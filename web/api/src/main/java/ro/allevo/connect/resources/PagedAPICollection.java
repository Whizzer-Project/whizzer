package ro.allevo.connect.resources;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;


public class PagedAPICollection<T extends BaseEntity> implements AutoCloseable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int page;
	private int pageSize;
	private List<T> items;
	private boolean hasMore;
	
	private Class<?> entityClass;
	
	private HttpHeaders httpHeaders;
	protected String url;
	
	public PagedAPICollection(String url, HttpHeaders httpHeaders, Class<?> entityClass) {
		this.url = url;
		this.httpHeaders = httpHeaders;
		this.entityClass = entityClass;
	}
	@JsonIgnore
	protected String getUrl() {
		return url;
	}
	@JsonIgnore
	protected void setUrl(String url) {
		this.url = url;
	}

	public boolean getHasMore() {
		return hasMore;
	}

	public Integer getTotal() {
		return items.size();
	}
	
	public List<T> getItems() {
		return items;
	}
	public List<T> asList() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getPageSize() {
		return pageSize;
	}
/*
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
*/
	@JsonIgnore
	protected Object getObject(UriInfo uriInfo, HttpMethod method) {
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
			return restTemplate.exchange(builder.toUriString(), method, entity,  entityClass).getBody();
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
		}
		return null;
	}
	
	public int getPage() {
		return page;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
}
