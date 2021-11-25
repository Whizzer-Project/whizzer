package ro.allevo.at.resources;

import javax.ws.rs.core.UriInfo;

import ro.allevo.at.model.InterfaceConfigEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.enums.QueryProvider;

public class InterfaceConfigResource extends PagedCollection<InterfaceConfigEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterfaceConfigResource(UriInfo uriInfo, QueryProvider queryProvider, URIFilter[] extraFilters) {
		super(uriInfo, queryProvider, extraFilters);
		// TODO Auto-generated constructor stub
	}

}
