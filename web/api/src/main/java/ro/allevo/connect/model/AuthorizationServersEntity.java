package ro.allevo.connect.model;

import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang3.StringUtils;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINCONNECT", name = "AUTHORIZATION_SERVERS")

@NamedQueries({
	@NamedQuery(name = "AuthorizationServersEntity.findAll", query = "select b from AuthorizationServersEntity b order by b.id"),
	@NamedQuery(name = "AuthorizationServersEntity.findTotal", query = "select count(b.id) from AuthorizationServersEntity b"),
	@NamedQuery(name = "AuthorizationServersEntity.findById", query = "select b from AuthorizationServersEntity b where b.id=:id"),
	@NamedQuery(name = "AuthorizationServersEntity.findByBIC", query = "select b from AuthorizationServersEntity b where b.bic=:bic")})

@Cacheable(false)
public class AuthorizationServersEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableGenerator(name="AidGenerator", table="FINCFG.IDGENLIST",
		pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
		pkColumnValue="CONNECT_ID")
	@Id
	@GeneratedValue(generator="AidGenerator")
	@Column(unique=true, nullable=false, updatable=false)
	@URLId
	private Long id;
	
	@Column(name = "client_id", nullable = false)
    private String clientId;
	
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;
    
    @Column(name = "user_id", nullable = false)
	private String userId;
    
	@Column(name = "user_secret")
	private String userSecret;
	
	@Column(name = "access_token_uri")
	private String accessTokenUri;
	
	@Column(name = "user_authorization_uri")
	private String userAuthorizationUri;
	
	@Column(name = "redirect_uri")
	private String redirectUri;
	
	@Column(name = "user_info_uri")
	private String userInfoUri;
	
	@Column(name = "resource_server_uri")
	private String resourceServerUri;
	
	@Column(name = "expiration_date")
	private Timestamp expirationDate;
	
	@Column(name = "grant_type")
	private String grantType;
	
	@Column(name = "authentication_scheme")
	private String authenticationScheme;
	
	@Column(name = "client_authentication_scheme")
	private String clientAuthenticationScheme;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "token_refresh")
	private String tokenRefresh;
	
	@Column(name = "bic")
	private String bic;
	
	@Column(name = "time_trigger")
	private String timeTrigger;
	
	@OneToOne(optional=false,fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="bic", insertable = false, updatable = false)
	private ConsentEntity consentEntity;
	
	@Column(name = " internalentities_id")
	private Integer internalEntityId;
	
//	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name="internalentities_id", referencedColumnName="id")
//	private InternalEntity internalEntity;

//	public void setInternalEntity(InternalEntity internalEntity) {
//		this.internalEntity = internalEntity;
//	}

	public Long getId() {
		return id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserSecret() {
		return userSecret;
	}

	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}

	public String getUserAuthorizationUri() {
		return userAuthorizationUri;
	}

	public void setUserAuthorizationUri(String userAuthorizationUri) {
		this.userAuthorizationUri = userAuthorizationUri;
	}

	public String getResourceServerUri() {
		return resourceServerUri;
	}

	public void setResourceServerUri(String resourceServerUri) {
		this.resourceServerUri = resourceServerUri;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getUserInfoUri() {
		return userInfoUri;
	}

	public void setUserInfoUri(String userInfoUri) {
		this.userInfoUri = userInfoUri;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getAuthenticationScheme() {
		return authenticationScheme;
	}

	public void setAuthenticationScheme(String authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}

	public String getClientAuthenticationScheme() {
		return clientAuthenticationScheme;
	}

	public void setClientAuthenticationScheme(String clientAuthenticationScheme) {
		this.clientAuthenticationScheme = clientAuthenticationScheme;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bankName) {
		this.bic = bankName;
	}

	public String getTokenRefresh() {
		return tokenRefresh;
	}

	public void setTokenRefresh(String tokenRefresh) {
		this.tokenRefresh = tokenRefresh;
	}

	public String getAispUrl() {
		if (null != this.consentEntity)
			return getUrl(this.consentEntity.getUrl());
		return "";
	}
	
	private String getUrl(String path) {
		if (null != path)
			return StringUtils.substringBeforeLast(path, "/");
		return "";
	}

	public String getTimeTrigger() {
		return timeTrigger;
	}

	public void setTimeTrigger(String timeTrigger) {
		this.timeTrigger = timeTrigger;
	}
	
	public ConsentEntity getConsentEntity() {
		return consentEntity;
	}

	public void setConsentEntity(ConsentEntity consentEntity) {
		this.consentEntity = consentEntity;
	}
	
	public Integer getInternalEntityId() {
		return internalEntityId;
	}

	public void setInternalEntityId(Integer internalEntityId) {
		this.internalEntityId = internalEntityId;
	}
	
//	public InternalEntity getInternalEntity() {
//		return internalEntity;
//	}

//	public void setInternalEntity(InternalEntity internalEntity) {
//		this.internalEntity = internalEntity;
//	}

	@Override
	public String toString() {
		return "AuthorizationServersEntity [id=" + id + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", userId=" + userId + ", userSecret=" + userSecret + ", accessTokenUri=" + accessTokenUri
				+ ", userAuthorizationUri=" + userAuthorizationUri + ", redirectUri=" + redirectUri + ", userInfoUri="
				+ userInfoUri + ", resourceServerUri=" + resourceServerUri + ", expirationDate=" + expirationDate
				+ ", grantType=" + grantType + ", authenticationScheme=" + authenticationScheme
				+ ", clientAuthenticationScheme=" + clientAuthenticationScheme + ", token=" + token + ", tokenRefresh="
				+ tokenRefresh + ", bic=" + bic + ", timeTrigger=" + timeTrigger + ", consentEntity=" + consentEntity
//				+ ", internalEntityId=" + internalEntityId
				+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "authorization servers";
	}
}
