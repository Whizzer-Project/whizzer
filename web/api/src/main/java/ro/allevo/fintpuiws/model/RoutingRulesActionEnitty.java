package ro.allevo.fintpuiws.model;

import java.io.IOException;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import ro.allevo.fintpuiws.util.DatasourceParser;

@Entity
@Table(schema = "FINCFG", name = "routingrulesactions")
@NamedQueries({
	@NamedQuery(name = "RoutingRulesActionEnitty.findAll", query = "SELECT a FROM RoutingRulesActionEnitty a order by a.displayOrder")
	})
public class RoutingRulesActionEnitty {
	
	@Id
	private int id;
	
	private String label;
	
	@Column(name = "displayorder")
	private int displayOrder;
	
	private String type;
	
	private String datasource;
	
	@Transient
	private Map<String, String> values;
	
	public String getLabel() {
		return label;
	}

	public String getType() {
		if (null == type || type.trim().isEmpty())
			return null;
		
		return type;
	}

	public Map<String, String> getDatasource() {
		return values;
	}
	
	public void fetchDatasource(EntityManager entityManager) throws IOException {
		values = DatasourceParser.parseAndFetch(datasource, entityManager);
	}
}
