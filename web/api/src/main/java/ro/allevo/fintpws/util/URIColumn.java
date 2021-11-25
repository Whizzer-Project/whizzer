package ro.allevo.fintpws.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class URIColumn {

	private String name;
	private List<String> values = new ArrayList<>();
	private Join<?,?> join;
	
	public URIColumn() {
		
	}
	
	public URIColumn(String name, String value) {
		this.name = name;
		this.values.add(value);
	}
	
	public static URIColumn tryParse(String field, Root<?> queryRoot) {
		URIColumn column = new URIColumn();
		column.parse(field, queryRoot);
		
		return column;
	}
	
	public void parse(String field, Root<?> queryRoot) {
		if (field.contains(".")) {
			String[] filterJoin = field.split("\\.");
			
			if (null != queryRoot) {
				Join<?, ?> join = queryRoot.join(filterJoin[0], JoinType.LEFT);
				int i;
				
				for (i=1; i<filterJoin.length-1; i++)
					join = join.join(filterJoin[i], JoinType.LEFT);
				
				field = filterJoin[i];
				setJoin(join);
			}
		}
		
		setName(field);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		try {
			return URLDecoder.decode(values.get(0), StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return values.get(0);
	}
	
	public void setValue(String value) {
		this.values.clear();
		this.values.add(value);
	}
	
	public List<String> getValues() {
		return values.stream().map(s -> {
			try {
				return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return s;
		}).collect(Collectors.toList());
	}
	
	public void setValues(Collection<String> values) {
		this.values.clear();
		addValues(values);
	}
	
	public void addValue(String value) {
		values.add(value);
	}
	
	public void addValues(Collection<String> values) {
		this.values.addAll(values);
	}

	public Join<?, ?> getJoin() {
		return join;
	}

	public void setJoin(Join<?, ?> join) {
		this.join = join;
	}
	
	public boolean hasJoin() {
		return null != join;
	}
	
}
