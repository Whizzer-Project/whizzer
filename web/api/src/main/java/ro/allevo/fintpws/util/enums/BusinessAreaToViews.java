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

package ro.allevo.fintpws.util.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import ro.allevo.fintpws.model.MsgTypeListEntity;
import ro.allevo.fintpws.resources.BusinessAreaResource;

/**
 * @version $Revision: 1.0 $
 * @author remus Enumeration of all VIEWS, mapping message types to Entities
 *
 */
public class BusinessAreaToViews extends AbstractView {

	/*
	 * BA_DI("Debit Instruments", "repstatdi"), BA_FT("Funds Transfer",
	 * "repstatft"), BA_TM("Treasury Markets", null), BA_ST("Statements",
	 * "repstatstm"), BA_MF("MicroFinance", "repstatmfi"), BA_PAYMENTS("Payments",
	 * "repstatpymt"),
	 * 
	 * BA_UNDEFINED("Undefined", null);
	 */

	private static final String SCHEMA = "findata";

	private List<MsgTypeListEntity> messageTypes = null;

	private static final List<BusinessAreaToViews> values = new ArrayList<BusinessAreaToViews>() {
		{
			add(new BusinessAreaToViews("Debit Instruments", "repstatdi"));
			add(new BusinessAreaToViews("Funds Transfer", "repstatft"));
			add(new BusinessAreaToViews("Treasury Markets", null));
			add(new BusinessAreaToViews("Statements", "repstatstmt"));
			add(new BusinessAreaToViews("MicroFinance", "repstatmfi"));
			add(new BusinessAreaToViews("Payments", "repstatpymt"));
			add(new BusinessAreaToViews("Invoices", "repstatinvc"));
			add(new BusinessAreaToViews("Events", "repevents"));
			add(new BusinessAreaToViews("Undefined", null));
			add(new BusinessAreaToViews("Outstanding", "repstatundif"));
		}
	};

	private static final Map<String, BusinessAreaToViews> stringToView = new HashMap<String, BusinessAreaToViews>() {
		{
			for (BusinessAreaToViews ba : values)
				put(ba.getName(), ba);
		}
	};

	private BusinessAreaToViews(String name, String view) {
		super(name, view, SCHEMA);
	}

	private void initMessageTypes(EntityManager entityManager) {
		if (null == this.messageTypes) {
			BusinessAreaResource ba = new BusinessAreaResource(null, entityManager, getName());
			messageTypes = ba.getMessageTypes().getMessageTypes().asList();
		}
	}

	public static BusinessAreaToViews get(EntityManager entityManager, String name) {
		BusinessAreaToViews ba = stringToView.get(name);

		if (null != ba) {
			ba.setEntityManager(entityManager);

			// get message types
			ba.initMessageTypes(entityManager);
		}
		return ba;
	}
	
	public static BusinessAreaToViews getView(EntityManager entityManager, String name) {
		BusinessAreaToViews ba = stringToView.get(name);

		if (null != ba) {
			ba.setEntityManager(entityManager);

			// get message types
//			ba.initMessageTypes(entityManager);
		}
		return ba;
	}

	@Override
	public Query getByIdQuery(HttpServletRequest request, String id) {
		Query query = super.getByIdQuery(request, id);
		if (null != query) {
		return query.setFirstResult(0).setMaxResults(1);
		}
		return query;
	}
/*
	@Override
	protected URIFilter[] getExtraFilters(HttpServletRequest request, UriInfo uriInfo) {
		Integer userId = (Integer) request.getSession().getAttribute("user_id");

		UserResource currentUser = new UserResource(uriInfo, getEntityManager(), userId);

		try {
			return new URIFilter[] { new URIFilter("entity", URIFilterType.FILTER_TYPE_EXACT) {
				{
					for (MsgTypeListEntity mt : messageTypes) {
						String messageType = mt.getMessageType();
						//addValues(currentUser.getAuthorizedEntities(messageType, UserRoleEntity.Action.VIEW));
					}
				}
			} };
		} finally {
			try {
				currentUser.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}*/
}
