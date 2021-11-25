package ro.allevo.fintpui.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class History {
	
	private String guid;
	@Size(min=1, max=35)
	private String batchid;
	@NotNull
	@Size(min=1, max=30)
	private String correlationid;
	@Size(min=1, max=40)
	private String feedback;
	@NotNull
	@Digits(integer=24, fraction = 2)
	private BigDecimal holdstatus;
	private Timestamp insertdate;
	@NotNull
	private String payload;
	@NotNull
	private long priority;
	@NotNull
	@Size(min=1, max=30)
	private String requestorservice;
	@NotNull
	@Size(min=1, max=30)
	private String requesttype;
	@Size(min=1, max=30)
	private String responderservice;
	@Digits(integer=12, fraction = 2)
	private BigDecimal sequence;
	@Size(min=1, max=30)
	private String sessionid;
	
	public String getGuid() {
		return guid;
	}

	public String getBatchid() {
		return batchid;
	}

	public String getCorrelationid() {
		return correlationid;
	}

	public String getFeedback() {
		return feedback;
	}

	public BigDecimal getHoldstatus() {
		return holdstatus;
	}

	public Timestamp getInsertdate() {
		return insertdate;
	}

	public String getPayload() {
		return payload;
	}

	public long getPriority() {
		return priority;
	}

	public String getRequestorservice() {
		return requestorservice;
	}

	public String getRequesttype() {
		return requesttype;
	}

	public String getResponderservice() {
		return responderservice;
	}

	public BigDecimal getSequence() {
		return sequence;
	}

	public String getSessionid() {
		return sessionid;
	}

	@JsonGetter("rowid")
	public String getId() {
		return guid;
	}
	
}
