package ro.allevo.fintpws.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class URIFilter extends URIColumn {
	/**
	 * Field FILTER. (value is ""filter_"")
	 */
	static final String FILTER = "filter_";
	
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public enum URIFilterType {
		FILTER_TYPE_EXACTOR("exctor"), //obsolete?
		FILTER_TYPE_CONTAINSOR("cntor"), //obsolete?
		FILTER_TYPE_EXACT("exact"), 
		FILTER_TYPE_NOTEXACT("nexact"), 
		FILTER_TYPE_CONTAINS(""), 
		FILTER_TYPE_END("end"),
		FILTER_TYPE_LNUMERIC("lnum"), //lower numeric
		FILTER_TYPE_UNUMERIC("unum"), //upper numeric
		FILTER_TYPE_LDATE("ldate"), //lower date
		FILTER_TYPE_UDATE("udate"), //upper date
		FILTER_TYPE_NOTNULL("nnull")// not null
		;


		String name;

		private URIFilterType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static URIFilterType fromName(String name) {
			for (URIFilterType enumVal : URIFilterType.values()) {
				if (name.equals(enumVal.name)) {
					return enumVal;
				}
			}
			return null;
		}
	}
	
	//private String name;
	private URIFilterType type;
	//private List<String> values = new ArrayList<String>();
	//private Join<?,?> join;
	
	public URIFilter() {
		
	}
	
	private URIFilter(String name) {
		//super();
		setName(name);
	}
	
	private URIFilter(String name, String value) {
		super(name, value);
	}
	
	public URIFilter(String name, String type, String value) {
		this(name, value);
		this.type = URIFilterType.fromName(type);
	}
	
	public URIFilter(String name, URIFilterType type) {
		this(name);
		this.type = type;
	}
	
	public URIFilter(String name, URIFilterType type, String value) {
		this(name, type);
		setValue(value);
	}

	public URIFilterType getType() {
		return type;
	}

	public void setType(URIFilterType type) {
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = URIFilterType.fromName(type);
	}
	
	public LocalDateTime getLocalDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return LocalDateTime.parse(getValue(), formatter);
	}
	
	public BigDecimal getBigDecimal() {
		return new BigDecimal(getValue());
	}
	
	public static List<URIFilter> parse(UriInfo uriInfo, Root<?> queryRoot) {
		List<URIFilter> entityFilterFields = new ArrayList<>();

		MultivaluedMap<String, String> queryParameters = null;
		try {
			queryParameters = uriInfo.getQueryParameters();
		}
		catch (Exception e) {
			queryParameters = new MultivaluedHashMap<>();
			e.printStackTrace();
		}		

		for (String filterField : queryParameters.keySet()) {
			
			if (filterField.startsWith(FILTER)) {
				
				URIFilter filter = new URIFilter();
				String[] fieldValues = filterField.split("_");
				
				String filterName = fieldValues.length > 1 ? fieldValues[1] : "";
				String type = fieldValues.length > 2 ? fieldValues[2] : "";
				
				filter.parse(filterName, queryRoot);
				
				filter.setType(type);
				filter.setValues(queryParameters.get(filterField));
				
				entityFilterFields.add(filter);
			}
		}
		return entityFilterFields;
	}
}
