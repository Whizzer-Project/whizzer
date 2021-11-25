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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.MsgTypeListEntity;
import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.resources.MessageTypesResource;
import ro.allevo.fintpws.resources.UserResource;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.URIFilter.URIFilterType;

/**
 * @version $Revision: 1.0 $
 * @author remus Enumeration of all VIEWS, mapping message types to Entities
 *
 */
public class MessageTypeToViews extends AbstractView {

	// TODO: should get from db
	/*
	 * MT_103("103", "Mt103View"), MT_104("104", "Mt104View"), MT_104R("104r",
	 * "Mt104rView"), MT_202("202", "Mt202View"),
	 * MT_FITOFICSTMRCDTTRF("fitoficstmrcdttrf", "MtFitoficstmrcdttrfView"),
	 * MT_COREBLKLRGRMTCDTTRF("coreblklrgrmtcdttrf", "MtCoreblklrgrmtcdttrfView"),
	 * MT_PN("pn", "MtPnView"), MT_RPN("rpn", "MtRpnView"), MT_BE("be", "MtBeView"),
	 * MT_RBE("rbe", "MtRbeView"), MT_CQ("cq", "MtCqView"), MT_RCQ("rcq",
	 * "MtRcqView"), MT_950("950", "Mt950View"), MT_950E("950e", "Mt950EView"),
	 * MT_940E("940e", "Mt940EView"), MT_940("940", "Mt940View"),
	 * MT_MFLD("mfiloandisb", "MtMfLdView"), MT_MFLR("mfiloanrepay", "MtMfLrView"),
	 * MT_UNDEFINED("undefined", "MtUndefinedView");
	 */

	private static final String SCHEMA = "findata";

	private static Map<String, MessageTypeToViews> stringToMessageType;

	private MessageTypeToViews(String type, String view) {
		super(type, view, SCHEMA);
	}

	private static void initMapping(EntityManager entityManager) {
		stringToMessageType = new HashMap<String, MessageTypeToViews>();

		MessageTypesResource messageTypesResource = new MessageTypesResource(null, entityManager);
		List<MsgTypeListEntity> messageTypes = messageTypesResource.asList();

		for (MsgTypeListEntity mt : messageTypes) {
			stringToMessageType.put(mt.getMessageType(),
					new MessageTypeToViews(mt.getMessageType(), mt.getReportingStorage()));
		}
	}

	public static MessageTypeToViews get(EntityManager entityManager, String type) {
		if (stringToMessageType == null) {
			initMapping(entityManager);
		}

		MessageTypeToViews mt = stringToMessageType.get(type);

		if (null != mt) {
			mt.setEntityManager(entityManager);
		}

		return mt;
	}
/*
	@Override
	protected URIFilter[] getExtraFilters(HttpServletRequest request, UriInfo uriInfo) {
		Integer userId = (Integer) request.getSession().getAttribute("user_id");

		UserResource currentUser = new UserResource(null, getEntityManager(), userId);

		try {

			String messageType = getName();

			return new URIFilter[] { new URIFilter("entity", URIFilterType.FILTER_TYPE_EXACT) {
				{
					setValues(currentUser.getAuthorizedEntities(messageType, UserRoleEntity.Action.VIEW));
				}
			} };
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				currentUser.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return null;
	}*/
}
