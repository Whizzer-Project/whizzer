package ro.allevo.fintpui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Config {

	@Value("${fintpui.api.url}")
	private String apiURL;
	
	@Value("${fintpui.api.auth-url}")
	private String authURL;
	
	@Value("${fintpui.api.tracker-url}")
	private String trackerURL;
	
	@Value("${fintpui.api.connect-url}")
	private String connectURL;
	
	@Value("${fintpui.api.at-url}")
	private String atURL;
	
	@Value("${fintpui.api.whizzer-url}")
	private String whizzerURL;
	
	@Value("${project.flow}")
	private String flow;
	
	@Value("${project.type}")
	private String projectType;
	
	
    public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	@Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource);
    }

    public String getMessage(String code) {
        return accessor.getMessage(code);
    }
    
    public String getAPIUrl() {
    	return apiURL;
    }
    
    public String getUiUrl() {
    	return apiURL.substring(0,apiURL.lastIndexOf("/api"))+"/ui-api";
    }

    public String getAPIAuthUrl() {
    	return authURL;
    }
    
    public String getTrackerUrl() {
    	return trackerURL;
    }
    
    public String getConnectUrl() {
    	return connectURL;
    }

	public String getAtURL() {
		return atURL;
	}
	
	public String getWhizzerURL() {
		return whizzerURL;
	}
	
	public String getFlow() {
		return flow;
	}
	
	public String getXML() {
		if (getProjectType().equalsIgnoreCase("adpharma")) {
			return "/Document/CstmrCdtTrfInitnOthr/PmtInf/CdtTrfTxInf/RmtInf/Ustrd";
		}
		return "/Document/CstmrCdtTrfInitnSala/PmtInf/CdtTrfTxInf/BgtCode";
	}

}
