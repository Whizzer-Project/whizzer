package ro.allevo.fintpws.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.reflect.FieldUtils;

import ro.allevo.fintpws.util.annotations.MergeEntity;

public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public abstract String toString();
	public abstract String getClassEvent();
	public abstract String getMessage();

	public void mergeWith(Object source) throws IllegalAccessException {
	    List<Field> columns = FieldUtils.getFieldsListWithAnnotation(this.getClass(), Column.class);
	    columns.addAll(FieldUtils.getFieldsListWithAnnotation(this.getClass(), MergeEntity.class));
	    
	    for(Field field  : columns) {
	    	Object value = FieldUtils.readField(field, source, true);
	    	
	    	//null values not sent in JSON
	    	if (null == field.getAnnotation(javax.persistence.Id.class)) {
		    	/*if (field.getType() == String.class) {
		    		//NULL string to null
		    		boolean isNull = String.valueOf(value).toUpperCase().equals("NULL");
		    		
		    		if (isNull)
		    			FieldUtils.writeField(field, this, null);
		    		else
		    			FieldUtils.writeField(field, this, value);
		    	}
		    	else*/
		    		FieldUtils.writeField(field, this, value);
	    	}
	    		
	    }
	    List<Field> joinClass = FieldUtils.getFieldsListWithAnnotation(this.getClass(), OneToOne.class);
        for(Field field: joinClass) {
            Object value = FieldUtils.readField(field, source, true);
            FieldUtils.writeField(field, this, value);
        }
        joinClass = FieldUtils.getFieldsListWithAnnotation(this.getClass(), ManyToOne.class);
        for(Field field: joinClass) {
            Object value = FieldUtils.readField(field, source, true);
            FieldUtils.writeField(field, this, value);
        }
	}
}
