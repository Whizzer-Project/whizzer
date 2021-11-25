package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalEntity {
	@NotNull
	@Size(min = 1, max = 35)
	private String name;
	@Size(max = 70)
	private String address;
	@Size( max = 35)
	private String city;	
	@Size(max = 2,message = "size must be 2")
	private String country;
	@NotNull
	@Size(min = 1, max = 10)
	private String fiscalCode;
	@Size(max = 35)
	private String email;
	//@Size(max = 20)

	@NotNull	
	private long id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFiscalCode() {
		return fiscalCode;
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}
	
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
	
}
