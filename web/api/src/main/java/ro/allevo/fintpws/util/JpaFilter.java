/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.util.URIFilter.URIFilterType;

public class JpaFilter<T> {

	/**
	 * Field LINK_HREF. (value is ""href"")
	 */
	static final String LINK_HREF = "href";
	/**
	 * Field LINK_REL. (value is ""rel"")
	 */
	static final String LINK_REL = "rel";
	/**
	 * Field SORT_FIELD. (value is ""sort_field"")
	 */
	// static final String SORT_FIELD = "sort_field";
	/**
	 * Field SORT_ORDER. (value is ""sort_order"")
	 */
	// static final String SORT_ORDER = "sort_order";
	/**
	 * Field SORT_ORDER_TYPE. (value is ""descending"")
	 */
	// static final String SORT_ORDER_TYPE = "descending";

	UriInfo uriInfo;
	EntityManager entityManager;
	Class<T> entity;
	CriteriaBuilder cb;
	CriteriaQuery<T> query;
	CriteriaQuery<Long> queryTotal;
	URIFilter[] apiFilters;
	
	boolean willSortDB = false;

	public JpaFilter(UriInfo uriInfo, EntityManager entityManager, Class<T> entity, URIFilter[] apiFilters) {
		this.entityManager = entityManager;
		this.entity = entity;
		this.uriInfo = uriInfo;
		this.apiFilters = apiFilters;
	}

	public TypedQuery<Long> getTotalQuery() {
		return entityManager.createQuery(queryTotal);
	}

	public TypedQuery<T> createQuery() {
		cb = entityManager.getCriteriaBuilder();

		query = cb.createQuery(entity);
		Root<T> queryRoot = query.from(entity);
		createSortedQuery(queryRoot);

		queryTotal = cb.createQuery(Long.class);
		queryTotal.select(cb.count(queryRoot));

		return createFilterQuery(queryRoot);
	}

	public void createSortedQuery(Root<T> queryRoot) {
		List<URISort> sortList = null;

		try {
			sortList = URISort.parse(uriInfo, queryRoot);
		} catch (Exception e) {
			sortList = new ArrayList<URISort>();
		}

		cleanColumns(sortList, queryRoot);

		List<Order> orderBy = new ArrayList<Order>();

		for (URISort sort : sortList) {
			willSortDB = true;

			Path<?> path = queryRoot;

			if (sort.hasJoin())
				path = sort.getJoin();

			switch (sort.getDirection()) {
			case "asc":
				orderBy.add(cb.asc(path.get(sort.getName())));
				break;
			case "desc":
				orderBy.add(cb.desc(path.get(sort.getName())));
				break;
			}
		}

		if (!orderBy.isEmpty())
			query.orderBy(orderBy);
	}

	private void cleanColumns(List<? extends URIColumn> columns, Root<T> queryRoot) {
		for (Iterator<? extends URIColumn> iter = columns.listIterator(); iter.hasNext();) {
			URIColumn filter = iter.next();

			try {
				// Check if there are field in entity
				if (filter.hasJoin())
					filter.getJoin().get(filter.getName());
				else
					queryRoot.get(filter.getName());

			} catch (IllegalArgumentException e) {
				// skip filter field for db
				iter.remove();
			}
		}
	}

	public TypedQuery<T> createFilterQuery(Root<T> queryRoot) {
		String fieldName = "";
		String fieldValue = "";
		URIFilterType filterType = null;
		Predicate predicate = cb.conjunction();

		List<URIFilter> filterList = URIFilter.parse(uriInfo, queryRoot);
		if(apiFilters!=null)
			Collections.addAll(filterList,  apiFilters);
		cleanColumns(filterList, queryRoot);
		int processedFilters = 0;

		for (URIFilter filter : filterList) {
			processedFilters++;

			fieldName = filter.getName();
			fieldValue = filter.getValue();
			filterType = filter.getType();

			Path<?> path = queryRoot;

			if (filter.hasJoin())
				path = filter.getJoin();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				if ("null".contains(fieldValue.toLowerCase())) {
					switch (filterType) {
					case FILTER_TYPE_EXACT:
						predicate = cb.and(predicate, path.get(fieldName).isNull());
						break;
					default:
						predicate = cb.and(predicate, path.get(fieldName).isNotNull());
						break;
					}
					
				} else {
					switch (filterType) {
					case FILTER_TYPE_EXACT:
						predicate = cb.and(predicate, path.get(fieldName).in(filter.getValues()));
						break;
					case FILTER_TYPE_NOTEXACT:
						predicate = cb.and(predicate, cb.notEqual(path.get(fieldName), fieldValue));
						break;
					case FILTER_TYPE_CONTAINS:
						predicate = cb.and(predicate,
								cb.like(cb.upper(path.<String>get(fieldName)), "%" + fieldValue.toUpperCase() + "%"));
						break;
					case FILTER_TYPE_EXACTOR:
						predicate = cb.or(predicate, cb.equal(path.get(fieldName), fieldValue));
						break;
					case FILTER_TYPE_CONTAINSOR:
						predicate = cb.or(predicate,
								cb.like(cb.upper(path.<String>get(fieldName)), "%" + fieldValue.toUpperCase() + "%"));
						break;
					case FILTER_TYPE_LNUMERIC:
						predicate = cb.and(predicate, cb.lessThan(path.<String>get(fieldName), fieldValue));
						predicate = cb.or(predicate, path.get(fieldName).isNull());
						break;
					case FILTER_TYPE_UNUMERIC:
						predicate = cb.and(predicate, cb.greaterThan(path.<String>get(fieldName), fieldValue));
						predicate = cb.or(predicate, path.get(fieldName).isNull());
						break;
					case FILTER_TYPE_LDATE:
						predicate = cb.and(predicate,
								cb.lessThanOrEqualTo(path.<Date>get(fieldName), sdf.parse(fieldValue)));
//						if(fieldName.equalsIgnoreCase("eventdate")) {
//							break;
//						}
						break;
					case FILTER_TYPE_UDATE:
						predicate = cb.and(predicate,
								cb.greaterThanOrEqualTo(path.<Date>get(fieldName), sdf.parse(fieldValue)));
						break;
					case FILTER_TYPE_END:
						predicate = cb.and(predicate, cb.lessThanOrEqualTo(path.<Timestamp>get(fieldName),
								ResourcesUtils.getTimestamp(fieldValue)));
						break;
					case FILTER_TYPE_NOTNULL:
						predicate = cb.and(predicate, cb.isNotNull(path.<String>get(fieldName)));
						break;
					default:
						switch (FilterJavaType.fromName(path.get(fieldName).getJavaType().toString())) {
						case FILTER_TYPE_BIGDECIMAL:
							predicate = cb.and(predicate, cb.equal(path.<BigDecimal>get(fieldName), fieldValue));
							break;
						case FILTER_TYPE_TIMESTAMP:
							predicate = cb.and(predicate, cb.greaterThanOrEqualTo(path.<Timestamp>get(fieldName),
									ResourcesUtils.getTimestamp(fieldValue)));
							break;
						default:
							break;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				processedFilters--;
			}
		}
		if (willSortDB || processedFilters > 0) {
			query.where(predicate);
			queryTotal.where(predicate);
			return entityManager.createQuery(query);
		}
		return null;
	}
}

enum FilterJavaType {
	FILTER_TYPE_TIMESTAMP("java.sql.Timestamp"), FILTER_TYPE_STRING("java.lang.String"),
	FILTER_TYPE_BIGDECIMAL("java.math.BigDecimal");

	String name;

	private FilterJavaType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static FilterJavaType fromName(String name) {
		for (FilterJavaType enumVal : FilterJavaType.values()) {
			if (name.contains(enumVal.name)) {
				return enumVal;
			}
		}
		return null;
	}
}
