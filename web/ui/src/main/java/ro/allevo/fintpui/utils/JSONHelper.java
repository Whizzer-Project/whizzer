package ro.allevo.fintpui.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {
	
	private JSONHelper() {
		throw new IllegalStateException("Utility class");
	}

	public static String toString(Object value) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(value);
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();

		Iterator<?> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = (String) keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

}
