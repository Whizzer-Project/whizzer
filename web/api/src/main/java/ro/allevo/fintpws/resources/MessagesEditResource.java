package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;

public class MessagesEditResource {
	
	private UriInfo uriInfo;
	private EntityManager emc;
	
	public MessagesEditResource(UriInfo uriInfo, EntityManager emc) {
		this.uriInfo = uriInfo;
		this.emc = emc;
	}
	
//	@Path("rules")
//	public EditRulesResource getEditRules() {
//		return new EditRulesResource(uriInfo, emc);
//	}
	
	@Path("rules")
	public EditRulesResource getEditRulesByTxType(@QueryParam("message_type") String msgType) {
		if (null == msgType) {
			return new EditRulesResource(uriInfo, emc);
		}
		return new EditRulesResource(uriInfo, emc, msgType);
	}

}
