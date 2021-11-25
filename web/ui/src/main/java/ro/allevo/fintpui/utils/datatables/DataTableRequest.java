package ro.allevo.fintpui.utils.datatables;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class DataTableRequest {

	private final NavigableMap<String, String[]> allRequestParams;
	private final List<DataTableRequestColumn> columns;

	private static final String API_FILTER_PREFIX = "filter_";
	private static final String API_FILTER_PAGE = "page";
	private static final String API_FILTER_PAGE_SIZE = "page_size";
	private static final String API_SORT_PREFIX = "sort_";
	private static final String COLUMNS = "columns[%s][%s]";
	private static final String COLUMNS_SEARCH = "columns[%s][%s][%s]";
	private static final String ORDER = "order[%s][%s]";

	public DataTableRequest(HttpServletRequest request) {
		allRequestParams = new TreeMap<>(request.getParameterMap());

		columns = getTableColumns();
	}

	public Map<String, List<String>> getApiParameters() {
		Map<String, List<String>> apiParams = new LinkedHashMap<String, List<String>>();

		if (!allRequestParams.containsKey("columns[0][data]"))
			return apiParams;

		apiParams.putAll(getFilterParameter());
		apiParams.putAll(getSortParameter());
		apiParams.putAll(getPaginationParameter());
		return apiParams;
	}

	private Double getDivideBy(int index) {
		Double rezult = 1.0;
		for (int ind = 0; ind < index; ind++) {
			rezult /= 10.0;
		}
		return rezult;
	}

	private String[] getConvertStringToIntFormatOfDateTime(String... values) {
		String[] rezult = new String[] { "1970", "01", "01", "00", "00", "00" };
		int iteration = 0;
		for (String value : values) {
			if (null != value && value.trim().length() > 0) {
				int sum = 0;
				Integer koef = 10;
				if (0 == iteration) {
					koef = 1000;
				}
				sum = castStringToNumber(value.trim(), koef);
				if (iteration < rezult.length)
					rezult[iteration] = String.valueOf(sum);
			}
			iteration++;

		}
		return rezult;
	}

	private int castStringToNumber(String value, int koef) {
		Double sum = 0d;
		if (value.length() == 1) {
			koef = 1;
		}
		for (int ind = 0; ind < value.length(); ind++) {
			char c = value.charAt(ind);
			if (c >= 48 && c <= 57) {
				sum += Character.getNumericValue(c) * (koef * getDivideBy(ind));
			}
		}
		return sum.intValue();
	}

	private Date getFormattedDate(String... dates) {
		Calendar c = Calendar.getInstance();
		if (null != dates[0] && dates[0].length() == 4) {
			c.set(Calendar.YEAR, castStringToNumber(dates[0], 1000));
		} else {
			return c.getTime();
		}
		for (int ind = 1; ind < dates.length; ind++) {
			if (null == dates[ind]) {
				return c.getTime();
			} else {
				switch (ind) {
				case 1:
					c.set(Calendar.MONTH, castStringToNumber(dates[ind], 10) - 1);
					break;
				case 2:
					c.set(Calendar.DAY_OF_MONTH, castStringToNumber(dates[ind], 10));
					break;
				case 3:
					c.set(Calendar.HOUR_OF_DAY, castStringToNumber(dates[ind], 10));
					break;
				case 4:
					c.set(Calendar.MINUTE, castStringToNumber(dates[ind], 10));
					break;
				default:
					c.set(Calendar.SECOND, castStringToNumber(dates[ind], 10));
				}
			}
		}
		return c.getTime();
	}

	private String[] getEndDate(String[] value) {
		value[3] = "23";
		value[4] = "59";
		value[5] = "59";
		return value;
	}

	public Map<String, List<String>> getFilterParameter() {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		String filter = "";

		// add column filters
		for (DataTableRequestColumn column : columns) {
			StringBuilder value = new StringBuilder();
			value.append(column.getSearch().getValue());
			String filterName = column.getFilterName();
			if (value.length() > 0) {
				if (null != filterName) {
					switch (filterName) {
					case "timeLimit":
						if (value.length() == 5) {
							value.append(":00");
						}
						if (value.length() == 2) {
							value.append(":00:00");
						}
						Timestamp timeStmp = Timestamp
								.valueOf(LocalDateTime.of(LocalDate.of(1970, 01, 01), LocalTime.parse(value)));
						filter = API_FILTER_PREFIX + column.getName() + column.getFilterType();
						params.put(filter, Arrays.asList(timeStmp.toString()));
						break;
					case "dateTimeLimit":
						String data = value.toString();
						String[] fullDateTime = new String[6];
						String time = null;
						if (value.toString().contains(" ")) {
							String[] splitValue = value.toString().split(" ");
							data = splitValue[0];
							time = splitValue[1];
						}
						if (data.contains("-")) {
							String[] fullData = data.split("-", 3);
							for (int ind = 0; ind < fullData.length; ind++) {
								fullDateTime[ind] = fullData[ind].trim().length() == 0 ? null : fullData[ind];
							}
						} else {
							fullDateTime[0] = data;
						}
						if (null != time && time.contains(":")) {
							String[] fullTime = time.split(":", 3);
							for (int ind = 0; ind < fullTime.length; ind++) {
								fullDateTime[ind + 3] = fullTime[ind].trim().length() == 0 ? null : fullTime[ind];
							}
						}
						String[] formattedDateTime = getConvertStringToIntFormatOfDateTime(fullDateTime);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						String startDate = sdf.format(getFormattedDate(formattedDateTime));
						String endDate = sdf.format(getFormattedDate(getEndDate(fullDateTime)));
						filter = API_FILTER_PREFIX + column.getName();
						if (value.toString().split("[-: ]").length < 5) {
							params.put(filter + "_udate", Arrays.asList(startDate));
							params.put(filter + "_ldate", Arrays.asList(endDate));
						} else {
							params.put(filter, Arrays.asList(startDate));
						}
						break;
					case "dateInterval":
						String[] dateInterval = value.toString().split(" - ");
						String startDateTime = dateInterval[0].replace(' ', 'T');
						String endDateTime = dateInterval[1].replace(' ', 'T');

						filter = API_FILTER_PREFIX + column.getName();
						params.put(filter + "_udate", Arrays.asList(startDateTime));
						params.put(filter + "_ldate", Arrays.asList(endDateTime));
						break;
					case "timeInterval":
						String[] dateTime = value.toString().split(" - ");
						String dt = dateTime[0];
						SimpleDateFormat sdfInterval = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar c = Calendar.getInstance();
						try {
							c.setTime(sdfInterval.parse(dt));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						c.add(Calendar.DATE, -1);
						dt = sdfInterval.format(c.getTime());
						String startDateTimeInterval = dt.replace(' ', 'T');
						String endDateTimeInterval = dateTime[1].replace(' ', 'T');

						filter = API_FILTER_PREFIX + column.getName();
						params.put(filter + "_udate", Arrays.asList(startDateTimeInterval));
						params.put(filter + "_ldate", Arrays.asList(endDateTimeInterval));
						break;
					default:
						if (!column.getName().contains(API_FILTER_PREFIX))
							filter = API_FILTER_PREFIX + column.getName() + column.getFilterType();
						else
							filter = column.getName() + column.getFilterType();

						params.put(filter, Arrays.asList(StringUtils.stripEnd(value.toString(), ".")));
					}
				} else {
					String[] values = value.toString().split("[|]+");
					String filterType = column.getFilterType();
					if (values.length>1) {
						filterType = "_cntor";
					}
					if (!column.getName().contains(API_FILTER_PREFIX))
						filter = API_FILTER_PREFIX + column.getName() + filterType;
					else
						filter = column.getName() + filterType;
//					for (String val : values)
						params.put(filter, Arrays.asList(values));
				}
			}
		}

		// add injected filters
		SortedMap<String, String[]> filters = allRequestParams.subMap(API_FILTER_PREFIX,
				API_FILTER_PREFIX + Character.MAX_VALUE);
		for (String key : filters.keySet())
			params.put(key, Arrays.asList(getRequestValue(key)));

		return params;
	}

	public Map<String, List<String>> getSortParameter() {
		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();

		int index = 0;

		while (allRequestParams.containsKey(String.format(ORDER, index, "column"))) {
			String sortColIndex = getRequestValue(String.format(ORDER, index, "column"));
			String colName = columns.get(Integer.parseInt(sortColIndex)).getName();
			String sortDirection = getRequestValue(String.format(ORDER, index, "dir"));

			String orderBy = API_SORT_PREFIX + colName;
			params.put(orderBy, Arrays.asList(sortDirection));

			index++;
		}

		return params;
	}

	public Map<String, List<String>> getPaginationParameter() {
		Map<String, List<String>> params = new HashMap<>();
		if (getRequestValue("start") != null && getRequestValue("length") != null) {
			int start = Integer.parseInt(getRequestValue("start"));
			int length = Integer.parseInt(getRequestValue("length"));
			params.put(API_FILTER_PAGE, Arrays.asList(String.valueOf(start / length + 1)));
			params.put(API_FILTER_PAGE_SIZE, Arrays.asList(String.valueOf(length)));
		}
		params.put("total", Arrays.asList(""));
		return params;
	}

	private List<DataTableRequestColumn> getTableColumns() {
		List<DataTableRequestColumn> columns = new ArrayList<DataTableRequestColumn>();
		int index = 0;
		// DataTableRequestColumn data = null;
		// Search search = null;
		while (allRequestParams.containsKey(String.format(COLUMNS, index, "data"))) {
			DataTableRequestColumn data = new DataTableRequestColumn();
			data.setName(getRequestValue(String.format(COLUMNS, index, "data")));
			data.setType(getRequestValue(String.format(COLUMNS, index, "type")));
			data.setFilterName(getRequestValue(String.format(COLUMNS_SEARCH, index, "type", "filterName")));
			data.setFilterType(getRequestValue(String.format(COLUMNS_SEARCH, index, "type", "filterType")));
			data.setOrderable(Boolean.valueOf(getRequestValue(String.format(COLUMNS, index, "orderable"))));
			data.setSearchable(Boolean.valueOf(getRequestValue(String.format(COLUMNS, index, "searchable"))));
			Search search = new Search();
			search.setValue(getRequestValue(String.format(COLUMNS_SEARCH, index, "search", "value")));
			search.setRegex(Boolean.valueOf(getRequestValue(String.format(COLUMNS_SEARCH, index, "search", "regex"))));
			data.setSearch(search);
			columns.add(data);
			index++;
		}
		return columns;
	}

	private String getRequestValue(String key) {
		if (allRequestParams.containsKey(key)) {
			return allRequestParams.get(key)[0];
		}
		return null;
	}

}
