package ro.allevo.fintpui.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Calendar {

	private Date nonbusinessdate;
	

	@Size(max = 50)
	private String description;
	

	public String getNonbusinessdate() {
		return nonbusinessdate.toString();
	}
	
	public Date getNonbusinessdateDateFormat() {
		return nonbusinessdate;
	}


	public void setNonbusinessdate(String nonbusinessdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = sdf.parse(nonbusinessdate);
		this.nonbusinessdate = new java.sql.Date(date.getTime());
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}



	
}
