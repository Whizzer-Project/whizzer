package ro.allevo.fintpui.utils;

import java.util.Comparator;

import ro.allevo.fintpui.model.TemplateConfigDetailed;

public class SortTemplateConfigDetailedById implements Comparator<TemplateConfigDetailed> {

	@Override
	public int compare(TemplateConfigDetailed o1, TemplateConfigDetailed o2) {
		return o1.getId() - o2.getId();
	}


}
