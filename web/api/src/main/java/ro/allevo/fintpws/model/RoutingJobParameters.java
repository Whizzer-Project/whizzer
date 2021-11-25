package ro.allevo.fintpws.model;


public class RoutingJobParameters {

		private String action;
		
		private String reason;
		
		private String actionDetails;
		
		private String messageType;
		
		private String[] messageIds;
		
		
		private String[] groupKeys;
		
		private String[] timeKeys;
		
		private String[][] fieldValues;
		
		private int userId;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getActionDetails() {
			return actionDetails;
		}

		public void setActionDetails(String actionDetails) {
			this.actionDetails = actionDetails;
		}

		public String getMessageType() {
			return messageType;
		}

		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String[] getMessageIds() {
			if (null != messageIds)
				return messageIds;
			
			return new String[0];
		}

		public void setMessageIds(String[] messageIds) {
			this.messageIds = messageIds;
		}

		public String[] getGroupKeys() {
			if (null != groupKeys)
				return groupKeys;
			
			return new String[0];
		}

		public void setGroupKeys(String[] groupKeys) {
			this.groupKeys = groupKeys;
		}

		public String[] getTimeKeys() {
			if (null != timeKeys)
				return timeKeys;
			
			return new String[0];
		}

		public void setTimeKeys(String[] timeKeys) {
			this.timeKeys = timeKeys;
		}

		public String[][] getFieldValues() {
			if (null != fieldValues)
				return fieldValues;
			
			return new String[0][0];
		}

		public void setFieldValues(String[][] fieldValues) {
			this.fieldValues = fieldValues;
		}
}
