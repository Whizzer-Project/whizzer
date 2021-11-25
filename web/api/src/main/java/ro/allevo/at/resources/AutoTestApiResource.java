package ro.allevo.at.resources;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Repository;

@Path("/automated-testing-api")
@Singleton
@Repository
public class AutoTestApiResource {

	@Context
	private UriInfo uriInfo;
	
	@PersistenceContext(unitName="fintpAT")
	public EntityManager entityManagerAutoTesting;
	
	@Path("tests")
	public TxProcessingTestsResource getTxProcessingTestResource() {
		return new TxProcessingTestsResource(uriInfo, entityManagerAutoTesting);
	}
}
