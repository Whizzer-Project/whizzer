package ro.allevo.fintpws.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.postgresql.util.PGobject;

import ro.allevo.fintpws.util.JSONHelper;


@Converter
public class DetailedConverter implements AttributeConverter<LinkedHashMap<String, Object>, Object> {

	  @Override
	  public Object convertToDatabaseColumn(LinkedHashMap<String, Object> objectValue) {
	        try {
	          PGobject out = new PGobject();
	          out.setType("json");
	          out.setValue(new JSONObject(objectValue).toString());
	          return out;
	        } catch (Exception e) {
	          throw new IllegalArgumentException("Unable to serialize to json field ", e);
	        }
	  }

	  @Override
	  public LinkedHashMap<String, Object> convertToEntityAttribute(Object dataValue) {
	      LinkedHashMap<String, Object> l = new  LinkedHashMap<String, Object>();
	      JSONObject json;
		try {
			json = new JSONObject(dataValue.toString());
			if (json instanceof JSONObject && json != JSONObject.NULL) {
		    	 try {
					l = JSONHelper.toMap(json);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	  
		      }
		} catch (org.codehaus.jettison.json.JSONException e1) {
			e1.printStackTrace();
		} catch (NullPointerException npe) {
			return null;
		}
	      return l;
	  }
	  
}
