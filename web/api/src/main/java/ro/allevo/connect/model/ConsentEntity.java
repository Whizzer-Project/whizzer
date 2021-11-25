package ro.allevo.connect.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

@Entity
@Table(schema = "finconnect", name = "consents")
@Cacheable(false)
@NamedQueries({ @NamedQuery(name = "ConsentEntity.findAll", query = "select b from ConsentEntity b"),
		@NamedQuery(name = "ConsentEntity.findTotal", query = "select count(b.bic) from ConsentEntity b"),
		@NamedQuery(name = "ConsentEntity.findById", query = "select b from ConsentEntity b where b.bic=:id")})

public class ConsentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bic")
	private String bic;

	@Column(name = "consent_id")
	private String consentId;

	@Column(name = "valid_until")
	private String validUntil;

	@Column(name = "url")
	private String url;


	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ConsentEntity [bic=" + bic + ", consentId=" + consentId + ", validUntil=" + validUntil + ", url=" + url
				+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "consent";
	}

}