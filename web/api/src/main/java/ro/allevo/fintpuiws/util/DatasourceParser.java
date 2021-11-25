package ro.allevo.fintpuiws.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class DatasourceParser {
	
	private static final String FROM = " from ";
	
	private DatasourceParser() {
	    throw new IllegalStateException("DatasourceParser class");
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject parseAndFetch(List<String> dataSources, EntityManager entityManager) {
		if (null == dataSources || dataSources.isEmpty())
			return null;
		JSONObject rezult = new JSONObject();
		for (String dataSource : dataSources) {
			String[] sources = dataSource.split("\\.");
			if (3 != sources.length)
				continue;
			String[] schemas = sources[0].split(":");
			String schema = schemas[1];
			String name = schemas[0];
			String table = sources[1];
			String values = sources[2];
			String[] columns;
			if (values.startsWith("[") && values.endsWith("]")) {
				values = values.substring(1, values.length() - 1);
				columns = values.split(",");
				
				StringBuilder querySB = new StringBuilder("select ");
				for (String column : columns) {
					querySB.append(column).append(", ");
				}
				String query = querySB.toString().substring(0,  querySB.length() - 2);				
				if (null != schema)
					table = schema + "." + table;
				
				if (sources.length == 3) {
					query += FROM + table + " order by " + columns[0];
				}else {
					query += FROM + table;
				}
				
				Query q = entityManager.createNativeQuery(query);
				List<Object[]> result = q.getResultList();
				
				JSONArray rezultValues = new JSONArray();
				for (Object[] items : result) {
					int ind = 0;
					JSONObject rowValues = new JSONObject();
					for (String column : columns) {
						column = correctionColumn(column);
						try {
							rowValues.put(column.replace("\"", "").trim(),  items[ind]!=null?items[ind].toString():"");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						ind++;
					}
					rezultValues.put(rowValues);
				}
				try {
					rezult.put(name, rezultValues);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return rezult;
	}
	
	private static String correctionColumn(String column) {
		switch (column.trim()) {
		case "bankbic":
			return "bic";
		case "fiscalcode":
				return "fiscalCode";
		case "accountnumber" :
			return "accountNumber";
		case "externalentityname":
			return "entityName";
		case "internalentityname":
			return "entityName";
		default : return column;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseAndFetch(String datasource, EntityManager entityManager) throws IOException {
		
		if (null == datasource || datasource.trim().isEmpty())
			return null;
		
		Map<String, String> values = new LinkedHashMap<>();
		
		if (datasource.startsWith("[")) {
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode nodes = mapper.readValue(datasource, ArrayNode.class);
			
			for (JsonNode node : nodes)
				if (node.isArray()) {
					ArrayNode keyvalue = (ArrayNode)node;
					values.put(keyvalue.get(0).asText(), keyvalue.get(1).asText());
				}
				else
					values.put(node.asText(), node.asText());
		}
		else {
			String[] source = datasource.split("\\.");
			
			String schema = null;
			String table = null;
			String value = null;
			
			if (source.length == 2) {
				table = source[0];
				value = source[1];
			}
			else if (source.length == 3) {
				schema = source[0];
				table = source[1];
				value = source[2];
			}
			
			if (null != table) {
				String[] columns;
				
				if (value.startsWith("[") && value.endsWith("]")) {
					value = value.substring(1, value.length() - 1);
					columns = value.split(",");
				}
				else columns = new String[] {value, value};
				
				String query = "select " + columns[0] + ", " + columns[1];
				
				if (null != schema)
					table = schema + "." + table;
				
				if (source.length == 3) {
					query += FROM + table + " order by " + columns[0];
				}else {
					query += FROM + table;
				}
				
				Query q = entityManager.createNativeQuery(query);
				List<Object[]> result = q.getResultList();
				
				for (Object[] items : result)
					values.put(items[0].toString(), items[1].toString());
			}
		}
		
		return values;
	}
}
