package ro.allevo.fintpuiws.resources;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ro.allevo.fintpuiws.model.FinTPAccessLists;
import ro.allevo.fintpuiws.util.DatasourceParser;

public class ConfigOptions {
	
	private EntityManager entityManager;

	/**
	 * @param entityManager
	 */
	public ConfigOptions(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Path("data")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getActions(FinTPAccessLists[] dataSources) {
		List<String> list = new ArrayList<>();
		for (FinTPAccessLists dataSource : dataSources) {
			list.add(dataSource.getFormattedData());
		}
		return DatasourceParser.parseAndFetch(list, entityManager).toString();
	}
}
