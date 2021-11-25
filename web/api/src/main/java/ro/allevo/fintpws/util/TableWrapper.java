package ro.allevo.fintpws.util;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableWrapper <T> {
	
	private boolean hasMore;
	
	private int total;
	
	private T[] items;

	@JsonGetter(value = "hasMore")
	public boolean hasMore() {
		return hasMore;
	}

	public int getTotal() {
		return total;
	}

	public T[] getItems() {
		return items;
	}
}
