package ro.allevo.fintpws.util.enums;

public class EventsLogger {
	
	public enum configUIClassEvents {

		INFO("Info"),
		ERROR("Error"),
		WARNING("Warning");
		 
		private String value;
		
	    private configUIClassEvents(String value) {
	       this.value = value;
	    }
	    public String getValue() {
	       return value;
	    }
	    
	    public String toString(){
	    	return value;
	    }
	 }
}
