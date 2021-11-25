package ro.allevo.fintpws.util;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

public class TokenUtils {
	
	public String getAuthToken() {
		String authToken = null;
		
	    try {
			InitialContext initialContext = new InitialContext();
			Context environmentContext = (Context) initialContext.lookup("java:/comp/env");
	
		    UriComponentsBuilder tokenUrlBuilder = UriComponentsBuilder.fromHttpUrl((String) environmentContext.lookup("tokenUrl"));
		    tokenUrlBuilder.queryParam("client_id", (String) environmentContext.lookup("client_id"));
		    tokenUrlBuilder.queryParam("redirect_uri", (String) environmentContext.lookup("redirect_uri"));
		    tokenUrlBuilder.queryParam("response_type", (String) environmentContext.lookup("response_type"));
		    tokenUrlBuilder.queryParam("state", (String) environmentContext.lookup("state"));
		    tokenUrlBuilder.queryParam("grant_type", (String) environmentContext.lookup("grant_type"));
		    tokenUrlBuilder.queryParam("username", (String) environmentContext.lookup("username"));
		    tokenUrlBuilder.queryParam("password", (String) environmentContext.lookup("password"));
		    		    
		    HttpClient httpclient = HttpClients.createDefault();
		    HttpPost httppost = new HttpPost(tokenUrlBuilder.toUriString());				
		    HttpResponse response = httpclient.execute(httppost);
		    HttpEntity entity = response.getEntity();			    
		    JSONObject json = new JSONObject(EntityUtils.toString(entity));
		    authToken = json.getString("access_token");     
		} catch (NamingException | JSONException | IOException e1) {
			LogManager.getLogger(getClass().getName()).debug(e1.getMessage()); 
			e1.printStackTrace();
		}
//	    LogManager.getLogger(getClass().getName()).debug("authToken = " + authToken);
	    return authToken;
	}

}
