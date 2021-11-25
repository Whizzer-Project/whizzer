package ro.allevo.fintpws.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.enums.EventsLogger;

public class ModelUtils {

	private ModelUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String toBoolYN(String value) {
		if (null != value && Boolean.TRUE.equals(value.equalsIgnoreCase("Y")))
			return "Y";
		return "N";
	}
	
	public static <T> StatusEntity getStatusEntityFromList(List<T> entities, UriInfo uriInfo, String restOperation) throws ParseException {
		// creates entity from first elem in list and the adds to Additionalinfo field, all details from all other entities (from toString method)
		
		StatusEntity statusEntityFromList = null;
		
		if(!entities.isEmpty()) {
			statusEntityFromList = getStatusEntity((BaseEntity) entities.get(0), uriInfo, restOperation);
		}
		
		StringBuilder builder = new StringBuilder();
		for(T entity : entities) {
			builder.append(entity.toString()).append(" ");			
		}
		
		if (null != statusEntityFromList)
			statusEntityFromList.setAdditionalinfo(builder.toString().trim());
		
		return statusEntityFromList;
		
	}
	
	public static StatusEntity getStatusEntity(BaseEntity entity, UriInfo uriInfo, String restOperation) throws ParseException {
		
		StatusEntity statusEntity = new StatusEntity();
		
	    // reflection ------  call method getClassEvent from class that extends BaseEntity
		statusEntity.setClasS((String)invokeObjectMethod(entity, "getClassEvent"));     
		
		statusEntity.setType(EventsLogger.configUIClassEvents.INFO.toString());
		
		Timestamp time = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
											.parse(LocalDateTime.now().toString()).getTime());
		statusEntity.setEventdate(time);
		statusEntity.setInsertdate(time);
		
		//getUser Id
		try {
			HttpServletRequest request = getCurrentHttpRequest();
			Integer userId = 0;
			if (null != request)
				userId = (Integer)request.getSession().getAttribute("user_id");
			statusEntity.setUserid(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		//SELECT oid, * FROM fincfg.servicemaps - for events id is 2
		statusEntity.setService(BigDecimal.valueOf(-1));

		// http://localhost:8085/fintp_ui/events?fp=header.menuReports&fp=header.menuEvents ---> localhost
		statusEntity.setMachine(uriInfo.getBaseUri().getHost());
		
		// message :   [Add new / Modify / Delete] + entity.getMessage()
		String entityMessage = "";
		if(isSpecialCharInMessage(entity.getMessage())){
			String instanceVar = getEntityInstanceVariablesFromMessage(entity.getMessage());
			Object methodResult = invokeObjectMethod(entity, "get" + instanceVar.substring(0, 1).toUpperCase() + instanceVar.substring(1));
			String replacement = "";
			
			if(methodResult instanceof String) {
				replacement = (String) methodResult;
			}else if(methodResult instanceof Integer){
				replacement = "" + (Integer)methodResult;
			}else {
				replacement = "" + methodResult;
			}
			
			entityMessage = updateEntityMessage(entity.getMessage(), instanceVar, replacement);
		}else {
			entityMessage = entity.getMessage();
		}
		if (entityMessage.isEmpty()) {
			return null;
		}
		statusEntity.setMessage(getTransformedRestOperationName(entity, restOperation) + " " + entityMessage); 		
		
		// Additional info : if restOp = delete , insert info from var with @Id
		if(restOperation.toLowerCase().equalsIgnoreCase("delete")) {
			statusEntity.setAdditionalinfo(getEntityIdFieldValue(entity));
		}else {
			statusEntity.setAdditionalinfo((String)invokeObjectMethod(entity, "toString")); 
		}
		
		// for EditedTransactionEntity update,  set with value from correlationid var
		if(restOperation.equalsIgnoreCase("put") && entity.getClass().getName().equalsIgnoreCase("EditedTransactionEntity")) {
			statusEntity.setCorrelationid((String)invokeObjectMethod(entity, "getCorrelationid"));
		}else {
			statusEntity.setCorrelationid("00000000-00000000-00000000");
		}
				
		statusEntity.setSessionid(null);
		statusEntity.setInnerexception(null);

		return statusEntity;
	}
	
	private static Object invokeObjectMethod(BaseEntity entity, String methodName) {
		
		Method[] allMethods = entity.getClass().getDeclaredMethods();
		Object result = null;
		
		for (Method classMethod : allMethods) {
			if(classMethod.getName().equalsIgnoreCase(methodName)) {
				try {
					result = classMethod.invoke(entity);
				} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
					e.printStackTrace();
				} 
			}
		}
		return result != null? result : "Error invoking entity method"  ;
	}
	
	private static String getTransformedRestOperationName(BaseEntity entity, String restOperation) {
		String transformedRestOp = null;
		switch (restOperation.toLowerCase()) {
			case "post":	
				if (null != entity && entity.getClass().getName().contains("UserRole")) {
					transformedRestOp = "Assign";
				}
				else {
					transformedRestOp = "Add new";
				}
				break;
			case "put":
				if (null != entity && entity.getClass().getName().contains("EditedTransaction")) {
					transformedRestOp = "";
				}else {
					transformedRestOp = "Modify";
				}
				break;
			case "delete":
				if (null != entity && entity.getClass().getName().contains("UserRole")) {
					transformedRestOp = "Remove";
				}
				else {
					transformedRestOp = "Delete";
				}
		}
		return transformedRestOp;
	}
	
	private static boolean isSpecialCharInMessage(String message) {
		return message.contains("<<");
	}
	
	private static String getEntityInstanceVariablesFromMessage(String message) {
		return StringUtils.substringBetween(message, "<<", ">>");
	}
	
	private static String updateEntityMessage(String message, String searchString , String replacement ) {
		return StringUtils.replace(message, "<<" + searchString + ">>", replacement);
	}
	
	private static String getEntityIdFieldValue(BaseEntity entity) {
		List<Field> columns = FieldUtils.getFieldsListWithAnnotation(entity.getClass(), javax.persistence.Id.class);
		Object value = null;
		try {
			value = FieldUtils.readField(columns.get(0), entity, true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		String idField = "";
		if(value != null) {
			if(value instanceof String) {
				idField = (String) value;
			}else if(value instanceof Integer) {
				int val = (Integer) value;
				idField = "" + val;
			}
		}
		return idField;
	}
	
	public static HttpServletRequest getCurrentHttpRequest(){
	    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
	    if (requestAttributes instanceof ServletRequestAttributes) {
	        return  ((ServletRequestAttributes)requestAttributes).getRequest();
	    }
	    return null;
	}
}
