package ro.allevo.fintpui.utils;

import java.util.Arrays;
import java.util.List;

public class Filters {
	
	private Filters() {
		throw new IllegalStateException("Filters class");
	}
	
	public static List<String> getFiltersParams(String... value) {
		return Arrays.asList(value);
	}
	
}
