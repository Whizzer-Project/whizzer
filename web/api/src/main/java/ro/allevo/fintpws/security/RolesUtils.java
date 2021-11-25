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

package ro.allevo.fintpws.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import ro.allevo.fintpws.util.URIFilter;

public class RolesUtils {
	
	public URIFilter[] getFilters(HttpServletRequest request, String projectType, EntityManager entityManager, int roleId) {
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		List<URIFilter> filters = new ArrayList<>();
		URIFilter[] filtersArray = null;
		if(projectType!=null && projectType.equals("whizzer")){
			TypedQuery<Long> roles = entityManager.createNamedQuery("UserRolesEntityMaps.findByUserIdAndRole", Long.class)
															.setParameter("userId", userId).setParameter("roleId", roleId);
			boolean isFirst = true;
			for(Long entityId:roles.getResultList()){
				URIFilter filter = new URIFilter();
				filter.setType(isFirst?"exact":"exctor");
				filter.setValue(String.valueOf(entityId));
				filter.setName("id");
				filters.add(filter);
				isFirst = false;
			}
			filtersArray = new URIFilter[filters.size()];
		}
		return filtersArray!=null?filters.toArray(filtersArray):null;
	}
}
