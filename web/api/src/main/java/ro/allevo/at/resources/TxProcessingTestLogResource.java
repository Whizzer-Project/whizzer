package ro.allevo.at.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.at.model.TxProcessingTestLogEntity;
import ro.allevo.fintpws.resources.PagedCollection;

public class TxProcessingTestLogResource extends PagedCollection<TxProcessingTestLogEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxProcessingTestLogResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestLogEntity.findAll", TxProcessingTestLogEntity.class),
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestLogEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				TxProcessingTestLogEntity.class);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertTxProcessingTestLogEntity(TxProcessingTestLogEntity txProcessingTestLogEntity) {
		// in fintpui RestApiDao<T> , in method:	public ResponseEntity<String> post(URI uri, Object entity) return client.postForEntity(uri, entity, String.class);
//		entity has txtype field with value, 
//		here txProcessingTestLogEntity is received with txtype field null
//		reason : both entities txProcessingTestLogEntity and Txprocessingtest have column with same name

		txProcessingTestLogEntity.setTxType(txProcessingTestLogEntity.getTxprocessingtest().getTxtype());
		return post(txProcessingTestLogEntity, false);
	}

}
