package ro.allevo.fintpws.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Root;
import javax.ws.rs.core.UriInfo;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class URISort extends URIColumn {

	static final String SORT = "sort_";
	
	private String direction = "asc";
	
	public static List<URISort> parse(UriInfo uriInfo, Root<?> queryRoot) throws IllegalArgumentException {
		
		if (null == uriInfo)
			throw new IllegalArgumentException("uriInfo cannot be null");
		
		List<URISort> sortList = new ArrayList<>();
	    
	    String queryString = uriInfo.getRequestUri().getQuery(); //NullPointerException
	    UriComponents components = UriComponentsBuilder.newInstance().query(queryString).build();
		
	    //preserves query params order
		MultiValueMap<String, String> queryParameters = components.getQueryParams();
				
		for (String sortField : queryParameters.keySet()) {
			
			if (sortField.startsWith(SORT)) {
				URISort sort = new URISort();
				
				String[] fieldValues = sortField.split("_");
				
				String columnName = fieldValues.length > 1 ? fieldValues[1] : "";
				
				sort.parse(columnName, queryRoot);
				
				sort.setDirection(queryParameters.get(sortField).get(0));
				
				sortList.add(sort);
			}
		}
		
		return sortList;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		if (direction.equals("desc"))
			this.direction = direction;
	}
}
