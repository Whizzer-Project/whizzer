package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINBO", name="MANUALTRANSACTIONS")

@NamedQueries({
	@NamedQuery(name = "PaymentEntity.findAll", query = "select b from PaymentEntity b"),
	@NamedQuery(name = "PaymentEntity.findTotal", query = "select count(b.id) from PaymentEntity b"),
	@NamedQuery(name = "PaymentEntity.findById", query = "select b from PaymentEntity b where trim(b.id)=:id") })
public class PaymentEntity extends BaseEntity {
	
	@Id
	@URLId
	@GeneratedValue(generator="PAYMENTS_ID_GENERATOR")
	@TableGenerator(name="PAYMENTS_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="PAYMENTS_ID") 
	private Integer id;
	
	@Column
	private String payload;
	
	@Column
	private Integer userid;
	
	@Column
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PaymentEntity [id=" + id + ", userid=" + userid + ", status=" + status + "]"; //", payload=" + payload +
	}

	@Override
	public String getClassEvent() {
		return Invariants.transactionClassEvents.OPERATE.toString();
	}
	
	@Override
	public String getMessage() {
		return "payments <<id>>";
	}
}
