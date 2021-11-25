package ro.allevo.connect.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

public class BaseQuartz<T> {

	public List<T> getFilteredList(UriInfo uriInfo, List<T> quartzEntitiesList) {
		
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters(); 
		List<T> filteredQuartzList = null;
		
		try {		
			List<Predicate<T>> allPredicates = new ArrayList<Predicate<T>>();
			boolean isOrdered = false;
			Method sortMethod = null;
			String sortKey = null;
			
			if(!queryParams.isEmpty()) {
				for(String key : queryParams.keySet()){	
					//http://localhost:8086/connect-api/jobs?sort_name=asc&total&page=1&page_size=10
					// key  ===   total  -- page -- page_size
					// value ==   ""     ---- 1   -----10
					String keyValue = queryParams.getFirst(key);
					
					if(!(key.contains("sort") || key.contains("filter"))) {
						continue;
					}
					
					Method testMethod = quartzEntitiesList.get(0).getClass().getMethod("get"+StringUtils.capitalize(StringUtils.substringAfterLast(key, "_")));

					if(key.contains("sort")) {
						isOrdered = true;
						sortMethod = testMethod;
						sortKey = keyValue;
					}
					
					if(key.contains("filter")) {					
						Predicate<T> predicate = job -> {
							try {								
								return ((String)testMethod.invoke(job)).toLowerCase().contains(keyValue.toLowerCase());	
							} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException  e) {
								e.printStackTrace();
								return false;
							}
						};
						allPredicates.add(predicate);
					}
				}
			}
			
			filteredQuartzList = quartzEntitiesList.stream().filter(allPredicates.stream().reduce(x->true, Predicate::and)).collect(Collectors.toList());

			if(isOrdered && filteredQuartzList.size() > 1) {

				Method getMethod = sortMethod;
				
				Comparator<T> quartzComparator = (job1,job2) -> {
					try {									
						return getMethod.invoke(job1).toString().compareTo(getMethod.invoke(job2).toString());
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
						return 1;
					} 
				};
				
				if(sortKey.equalsIgnoreCase("asc")) {
					filteredQuartzList = filteredQuartzList.stream().sorted(quartzComparator).collect(Collectors.toList());
				}
				else{
					filteredQuartzList = filteredQuartzList.stream().sorted(quartzComparator.reversed()).collect(Collectors.toList());
				}				
			}
			
			
		} catch (NoSuchMethodException|SecurityException e) {
			e.printStackTrace();
		}			
		return queryParams.isEmpty() ? quartzEntitiesList : filteredQuartzList ;
	}
	
	public List<T> getPage(List<T> filteredQuartzList, int page, int page_size){
		return filteredQuartzList.stream().skip((page-1) * page_size).limit(page_size).collect(Collectors.toList());
	}
	
	public int getValueOfQueryParameter(UriInfo uriInfo, String queryParameter) {
		
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		
		int result = 0;
		if(queryParameter.equals("page")) {
			result = 1;
		}
		if(queryParameter.equals("page_size")) {
			result = 10;
		}
		
		if(!queryParams.isEmpty()) {
			for(String key : queryParams.keySet()) {
				String keyValue = queryParams.getFirst(key);
				if(key.equals(queryParameter)) {
					result = Integer.parseInt(keyValue);
					break;
				}
			}
		}
		return result;
	}
}
