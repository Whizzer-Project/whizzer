package ro.allevo.fintpui.utils;

import java.util.Comparator;
import java.util.HashMap;

public class SortListBankByName implements Comparator<HashMap<String, Object>> {

	private static final String NAME = "name";
	@Override
	public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
		return o1.get(NAME).toString().compareToIgnoreCase(o2.get(NAME).toString());
	}
}
