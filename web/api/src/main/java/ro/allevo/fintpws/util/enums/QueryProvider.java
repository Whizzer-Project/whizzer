package ro.allevo.fintpws.util.enums;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.util.URIFilter;

public interface QueryProvider {
	public Query getItemsQuery(HttpServletRequest request, UriInfo uriInfo, URIFilter... extraFilters);
	public Query getTotalQuery(HttpServletRequest request, UriInfo uriInfo, URIFilter... extraFilters);
	public Query getByIdQuery(HttpServletRequest request, String id);
}
