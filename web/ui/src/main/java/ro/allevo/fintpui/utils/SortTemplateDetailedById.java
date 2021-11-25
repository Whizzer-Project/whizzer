package ro.allevo.fintpui.utils;

import java.util.Comparator;

import ro.allevo.fintpui.model.TemplateDetailed;

public class SortTemplateDetailedById implements Comparator<TemplateDetailed>
{
	@Override
	public int compare(TemplateDetailed o1, TemplateDetailed o2) {
		return o1.getId() - o2.getId();
	}

}
