package ro.allevo.connect.schedulers;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.connect.model.AmqMessage;
import ro.allevo.connect.model.Transaction;
import ro.allevo.connect.model.TransactionEntity;
import ro.allevo.connect.model.TransactionsEntity;
import ro.allevo.fintpws.model.InternalEntity;
import ro.allevo.fintpws.util.TokenUtils;

@Component
public class AccountSatementJob implements Job {

	private List<String> resouceIds;
	private JobExecutionContext context;
	private String apiUrl;
	private String url;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		init(context);
		for(String resourceId:resouceIds){
			getStatement(resourceId);
		}
	}
	
	private void getStatement(String resourceId) {		
		RestTemplate restTemplate = new RestTemplate();	
		TokenUtils tokenUtils = new TokenUtils();
		String token = tokenUtils.getAuthToken();
 
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
	    builder.path("accounts/").path(resourceId).path("/transactions");
	    builder.queryParam("dateFrom", getDateFrom());
		builder.queryParam("dateTo", ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
				
		ResponseEntity<TransactionsEntity> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,  null,  new ParameterizedTypeReference<TransactionsEntity>(){});

	    TransactionsEntity transactionsEntity = response.getBody();
	    List<TransactionEntity> transactions = transactionsEntity.getBooked();
	   
	    for(TransactionEntity transaction:transactions) {
	    	StringWriter sw = new StringWriter();
	    	AmqMessage message = new AmqMessage();
	    	message.setId(transaction.getTransactionId());
	    	Transaction tran = new Transaction();
	    	tran.setTransactionId(transaction.getTransactionId());
	    	
	    	String transactionName = "";
	    	if(transaction.getCreditorName().length() > 0) {
	    		transactionName = transaction.getCreditorName();
	    		tran.setCreditorName(transactionName);
	    	}else {
	    		transactionName = transaction.getDebtorName();
	    		tran.setDebtorName(transactionName);
	    	}	    		    	
	    	
	    	InternalEntity internalEntity = getInternalEntityFromHttpFilteredRequest(token, transactionName);
	    	if(internalEntity != null) {
				tran.setInternalEntityId("" + internalEntity.getId());
				tran.setInternalEntityName(internalEntity.getName());
				tran.setInternalEntityCity(internalEntity.getCity());
				tran.setInternalEntityCountry(internalEntity.getCountry());
				tran.setInternalEntityAddress(internalEntity.getAddress());
	    	}
	        
	    	tran.setTransactionAmount(transaction.getTransactionAmount());
	    	tran.setBookingDate(transaction.getBookingDate());
	    	tran.setValueDate(transaction.getValueDate());	    		    	
	    	tran.setDetails(transaction.getDetails());	    	    	
	    	tran.setIbanAccount(transaction.getIbanAccount());    	
	    	
	    	TimeZone tz = TimeZone.getTimeZone("UTC");
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); 
	    	df.setTimeZone(tz);
	    	tran.setSystemDateTime(df.format(new Date()));
	    	
	    	tran.setFromDate(getDateFrom());
	    	tran.setToDate(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
	    	tran.setAccountId(transactionsEntity.getAccount()!=null?transactionsEntity.getAccount().getIban():"");	    	
	    	tran.setRemittanceInformationUnstructured(transaction.getRemittanceInformationUnstructured());
	    	
	    	try {
	            JAXBContext context = JAXBContext.newInstance(Transaction.class);
	            Marshaller marshaller = context.createMarshaller();
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            marshaller.marshal(tran, sw);
		    } catch (JAXBException e) {
		            e.printStackTrace();
		    }
	    	message.setText(sw.toString());
	    	restTemplate.postForEntity(apiUrl + "/connect-api/jms/sendToAmq", message, AmqMessage.class);
	    }
	}
	
	private String getDateFrom() {
		CronTrigger cronTrigger = (CronTrigger) context.getTrigger();
        String cronExpression = cronTrigger.getCronExpression();
	    CronExpression cron = null;
	    try {
			cron = new CronExpression(cronExpression);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DATE, -1);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	    return  sdf.format(cron.getTimeAfter(calendar.getTime()));
	}
	
	@SuppressWarnings("unchecked")
	private void init(JobExecutionContext context) {
		this.context = context;
		JobDetail job = context.getJobDetail();
	    JobDataMap parameters = job.getJobDataMap();
	    this.resouceIds = (List<String>) parameters.get("resourceIds");
	    this.url = parameters.getString("url");
	    this.apiUrl = parameters.getString("apiUrl");
	}	
	
	private InternalEntity getInternalEntityFromHttpFilteredRequest(String token, String transactionName) {
		RestTemplate restTemplate = new RestTemplate();	
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        
		try {
	        ResponseEntity<Object> responseEntity = restTemplate.exchange(apiUrl + "/api/internal-entities?filter_name_exact=" + transactionName , HttpMethod.GET, httpEntity, Object.class);
	        Object result = responseEntity.getBody();

			if (result instanceof LinkedHashMap<?, ?>) {
				LinkedHashMap<?, ?> jsObj = (LinkedHashMap<?, ?>) result;
				Object itemsObj = jsObj.get("items");

				if (itemsObj instanceof ArrayList<?>) {
					ArrayList<?> responseItemsList = (ArrayList<?>) itemsObj;
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					InternalEntity internalEntity = mapper.convertValue(responseItemsList.get(0), InternalEntity.class);
					return internalEntity;
				}
			} 
		} catch (Exception e) {
			System.out.println("getInternalEntityFromHttpFilteredRequest() threw exception, message = " + e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

}
