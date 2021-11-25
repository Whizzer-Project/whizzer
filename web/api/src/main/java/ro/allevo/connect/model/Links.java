package ro.allevo.connect.model;

public class Links {
	private Link scaOAuth;
	private Link self;
	private Link status;

	public Link getScaOAuth() {
		return scaOAuth;
	}

	public void setScaOAuth(Link scaOAuth) {
		this.scaOAuth = scaOAuth;
	}

	public Link getSelf() {
		return self;
	}

	public void setSelf(Link self) {
		this.self = self;
	}

	public Link getStatus() {
		return status;
	}

	public void setStatus(Link status) {
		this.status = status;
	}
}