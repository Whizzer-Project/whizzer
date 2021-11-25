package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.FinTPAccessLists;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Utils;

@Service
public class TemplateConfigOptionsRestApiDao extends RestApiDao<TemplateConfigOptions>
		implements TemplateConfigOptionsDao {

	@Autowired
	Config config;

	public TemplateConfigOptionsRestApiDao() {
		super(TemplateConfigOptions.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-options").build();
	}

	@Override
	public TemplateConfigOptions[] getAllTemplateConfingOptions() {
		return getAll();
	}

	@Override
	public Map<Integer, List<String>> getAllConfingOptionsValues() {

		TemplateConfigOptions[] templatesOptions = getAll();

		Map<Integer, List<String>> optionsValues = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter stringWriter = new StringWriter();
		// JSONArray arrayLists = null;
		// List<String> result = null;
		// PagedCollection<?> pc = null;
		// URI uri = null;
		try {
			for (TemplateConfigOptions templateOption : templatesOptions) {
				String[] option = templateOption.getDatasource().split("\\.");
				if (option.length == 2) {
					URI uri = UriBuilder.fromUri(config.getAPIUrl()).path(option[0]).build();
					PagedCollection<?> pc = getObject(uri, PagedCollection.class);
					objectMapper = new ObjectMapper();
					stringWriter = new StringWriter();
					objectMapper.writeValue(stringWriter, pc.getItems());
					JSONArray arrayLists = new JSONArray(stringWriter.toString());
					List<String> result = new LinkedList<>();
					for (int i = 0; i < arrayLists.length(); i++) {
						String val = arrayLists.getJSONObject(i).optString(option[1]);
						if (val.length() > 0)
							result.add(val);
					}
					optionsValues.put(templateOption.getId(), result);
				}

			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return optionsValues;
	}

	@Override
	public JSONObject getAllConfingOptionsValues(Set<String> setEntity){

//		Map<String, List<Object>> optionsValues = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
//		StringWriter stringWriter = new StringWriter();
//			String correctEntity = null;
			List<FinTPAccessLists> dataSource = new ArrayList<>();
			for (String entity : setEntity) {
				List<FinTPAccessLists> finTPAccessLists = Utils.finTPAccessLists;
//				correctEntity = finTPLists.stream()
//						.filter(list -> entity.toLowerCase().equals(list.getName()))
//						.findFirst()
//						.orElse(null)
//						.getApiServiceName();
				dataSource.add(finTPAccessLists.stream().filter(list -> entity.toLowerCase().equals(list.getName())).findFirst().orElse(null));
//				URI uri = UriBuilder.fromUri(config.getAPIUrl()).path(correctEntity).build();
//				PagedCollection<?> pc = getObject(uri, PagedCollection.class);
//				objectMapper = new ObjectMapper();
//				stringWriter = new StringWriter();
//				objectMapper.writeValue(stringWriter, pc.getItems());
//				ResponseEntity<String> response = client.getForEntity(uri, String.class);
//				ObjectMapper mapper = new ObjectMapper();
//				List<HashMap<String, Object>> rezult;
//					rezult =  mapper.readValue(response.getBody(),
//							mapper.getTypeFactory().constructParametricType(List.class, HashMap.class));
//				
//				optionsValues.put(entity.toLowerCase(), JSONHelper.toList( new JSONArray(response.getBody())));
//				optionsValues.put(entity.toLowerCase(), JSONHelper.toList(new JSONArray(stringWriter.toString())));
			}
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			String jsonString = "";
			try {
				jsonString = objectMapper.writeValueAsString(dataSource);
				try {
					@SuppressWarnings("unused")
					org.json.JSONArray jSon = new org.json.JSONArray(jsonString);
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				URI uri = UriBuilder.fromUri(config.getUiUrl()).path("config-options").path("data").build();
				ResponseEntity<String> res = post(uri, dataSource.toArray()); 
				JSONObject json = null;
				try {
					json = new JSONObject(res.getBody());
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Iterator<String> keys = json.keySet().iterator();
//				while(keys.hasNext()) {
//					String key = keys.next();
//					json.get(key);
//					
//				}
				return json;
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}

	public Object[] getConfigOption(String condition, String queryValue) {
		JSONObject jsonCondition = null;
		try {
			jsonCondition = new JSONObject(condition);
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.json.JSONArray rules = null;
		try {
			rules = jsonCondition.getJSONArray("rules");
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bussList = "";
		String bussField = "";
		for(int i=0;i<rules.length();i++) {
			JSONObject rule = null;
			try {
				rule = rules.getJSONObject(i);
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] buss = null;
			try {
				buss = rule.getString("field").split("-");
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bussList = buss[0];
			bussField = buss[1];
		}
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path(bussList).queryParam(bussField, queryValue).build();
		
		PagedCollection<?> pc = getObject(uri, PagedCollection.class);

		return pc.getItems();
	}

}
