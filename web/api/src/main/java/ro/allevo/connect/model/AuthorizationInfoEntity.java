package ro.allevo.connect.model;

public class AuthorizationInfoEntity {
	
	private String  authorization_endpoint;
	private String  token_endpoint;
	
	public String getAuthorization_endpoint() {
		return authorization_endpoint;
	}
	public void setAuthorization_endpoint(String authorization_endpoint) {
		this.authorization_endpoint = authorization_endpoint;
	}
	public String getToken_endpoint() {
		return token_endpoint;
	}
	public void setToken_endpoint(String token_endpoint) {
		this.token_endpoint = token_endpoint;
	}
	
	
}
