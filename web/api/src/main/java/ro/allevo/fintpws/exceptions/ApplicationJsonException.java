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

package ro.allevo.fintpws.exceptions;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.ValidationException;
import org.postgresql.util.PSQLException;

import ro.allevo.fintpws.util.ResourcesUtils;

/**
 * Class that holds the required response fields ( message, code ). Used by the
 * custom exception mappers to fill the response body.
 */
@XmlRootElement(name = "error")
public class ApplicationJsonException extends WebApplicationException {
	
	public enum Reason {
		DB_ForeignKey("DB_ForeignKey"),
		DB_PrimaryKey("DB_PrimaryKey"),
		DB_UniqueKey("DB_UniqueKey"),
		DB_Other("DB_Other");
		
		private String code;
		
		private Reason(String code) {
			this.code = code;
		}
	}
	
	private static final long serialVersionUID = 1L;

	//private String message;
	
	//private Status code;
	
	private Reason reason;
	
	private Map<String, ?> extraInfo;
	
	/**
	 * Constructor for ApplicationJsonException.
	 * 
	 * @param message
	 *            String
	 * @param code
	 *            int
	 */
	/*@Deprecated
	public ApplicationJsonException(Throwable cause, String message, int code) {
		super(cause, JsonResponseWrapper.getResponse(Response.Status.fromStatusCode(code), message, null, null));
	}*/
	
	public ApplicationJsonException(String message, Status code) {
		super(message, code);
	}
	
	public ApplicationJsonException(Throwable cause, String message, Status code) {
		super(message, cause, code);
	}
	
	public ApplicationJsonException(Throwable cause, String message, Status code, Reason reason) {
		this(cause, message, code);
		
		this.reason = reason;
	}
	
	public ApplicationJsonException(Throwable cause, String message, Status code, Reason reason, Map<String, ?> extraInfo) {
		this(cause, message, code);
		
		this.reason = reason;
		this.extraInfo = extraInfo;
	}
	
	@SuppressWarnings("rawtypes")
	public static void handleSQLException(PSQLException t, String errorContext, Logger logger, 
			Class entityClass) {
		final String sqlState = t.getSQLState();
		logger.error(errorContext + t.getMessage(), t);
		//check error belongs to integrity constraint class
		if (sqlState.equals("23505")) { //duplicate key
			//parse error message
			//Detail: Key (queueid, sequence)=(1, 0) already exists.
			
			Pattern pattern = Pattern.compile("\\((.*)\\)=\\((.*)\\)");
			Matcher matcher = pattern.matcher(t.getMessage());
			
			Map<String, Object> info = null;
			List<Serializable> columnsAndValues = new ArrayList<Serializable>();
			
			if (matcher.find()) {
				String[] columns = matcher.group(1).split(",");
				String[] values = matcher.group(2).split(",");
				
				for (int i=0; i<columns.length; i++) {
					String column = columns[i].trim();
					String resolvedColumn = ResourcesUtils.getColumnFromEntity(entityClass, column);
					final String columnName = (resolvedColumn != null) ? resolvedColumn : column;
					final String columnValue = values[i].trim();
					
					columnsAndValues.add(new Serializable() {
						public String getName() {
							return columnName;
						}
						public String getValue() {
							return columnValue;
						}
					});
				}
				
				
				
				info = new HashMap<String, Object>(){{
					put("columns", columnsAndValues);
				}};
			}
			
			throw new ApplicationJsonException(
					t, 
					errorContext + t.getMessage(), 
					Status.CONFLICT,
					Reason.DB_PrimaryKey,
					info
					);
		}
		else if (sqlState.equals("23503")) { //foreign key
			//parse error message 
			//Detail: Key (bic)=(AAAAAAAA) is still referenced from table "internalaccounts".
			Pattern pattern = Pattern.compile("Detail: Key \\((.*)\\)=\\((.*)\\) is still referenced from table \\\"(.*)\\\"");
			Matcher matcher = pattern.matcher(t.getMessage());
			
			Map<String, String> info = null;
			
			if (matcher.find()) {
				String column = matcher.group(1);
				String columnName = ResourcesUtils.getColumnFromEntity(entityClass, column);
				String value = matcher.group(2);
				String table = matcher.group(3);
				
				info = new HashMap<String, String>(){{
					put("column", columnName != null ? columnName : column);
					put("value", value);
					put("table", table);
				}};
			}
			
			throw new ApplicationJsonException(
					t, 
					errorContext + t.getMessage(), 
					Status.CONFLICT,
					Reason.DB_ForeignKey,
					info
					);
		}
		else if (sqlState.startsWith("23")) {
			throw new ApplicationJsonException(
					t, 
					errorContext + t.getMessage(),
					Response.Status.CONFLICT,
					Reason.DB_Other
					);
		} else {
			throw new ApplicationJsonException(
					t, 
					errorContext + t.getMessage(),
					Response.Status.BAD_REQUEST);

		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void handleSQLException(RollbackException re,
			String errorContext, Logger logger, Class entityClass) {
		// traverse the cause to find a possible constraint violation
		Throwable t = re.getCause();
		while (null != t) {
			if (t instanceof PSQLException) {
				handleSQLException((PSQLException)t, errorContext, logger, entityClass);
			}
			
			if (t instanceof SQLIntegrityConstraintViolationException) {
				logger.error(errorContext + t.getMessage(), t);
				throw new ApplicationJsonException(
						re, 
						errorContext + t.getMessage(),
						Response.Status.CONFLICT);
			}
			// TODO: check if necessary to go one level lower (instanceof
			// IntegrityException, or DescriptorExcettion, etc)
			if(t instanceof ValidationException){
				logger.error(errorContext + t.getMessage(), t);
				throw new ApplicationJsonException(
						re, 
						errorContext + t.getMessage(),
						Response.Status.CONFLICT);
			}
			
			if (t instanceof SQLException) {
				logger.error(errorContext + t.getMessage(), t);
				throw new ApplicationJsonException(
						re, 
						errorContext + t.getMessage(),
						Response.Status.BAD_REQUEST);
			}
			t = t.getCause();
		}
	}

	public static void handleSQLException(PersistenceException e,
			String errorContext, Logger logger) {
		//this exception occurs when data_changed exception is thrown
		
		Throwable t = e.getCause();
		logger.error(errorContext + t.getMessage(), t);
		if(t.getMessage() != null && t.getMessage().contains("20409"))
			throw new ApplicationJsonException(
					e, 
					errorContext + t.getMessage(),
					Response.Status.CONFLICT);
		else
			throw new ApplicationJsonException(
					e, 
					errorContext + t.getMessage(),
					Response.Status.NOT_ACCEPTABLE);
	}
	
	public static void handleSQLException(DatabaseException e,
			String errorContext, Logger logger) {
		//this exception occurs when data_changed exception is thrown
		
		Throwable t = e.getCause();
		logger.error(errorContext + t.getMessage(), t);
		if(t.getMessage() != null && t.getMessage().contains("20409"))
			throw new ApplicationJsonException(
					e, 
					errorContext + t.getMessage(),
					Response.Status.CONFLICT);
		else
			throw new ApplicationJsonException(
					e, 
					errorContext + t.getMessage(),
					Response.Status.NOT_ACCEPTABLE);
	}
	
	@Override
	public Response getResponse() {
		return Response
				.status(super.getResponse().getStatus())
				.entity(getEntity())
				.build();
	}

	public Reason getReason() {
		return reason;
	}

	public Map<String, ?> getExtraInfo() {
		return extraInfo;
	}
	
	private Serializable getEntity() {
		final ApplicationJsonException myException = this;
		final Response response = super.getResponse();
		
		
		return new Serializable() {
			private static final long serialVersionUID = 1L;
			
			public String getMessage() {
				return myException.getMessage();
			}

			public int getCode() {
				return response.getStatus();
			}
			
			public Reason getReason() {
				return myException.getReason();
			}
			
			public Map<String, ?> getExtraInfo() {
				return myException.getExtraInfo();
			}
		};
	}
}
