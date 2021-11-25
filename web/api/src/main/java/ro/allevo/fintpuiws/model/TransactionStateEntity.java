package ro.allevo.fintpuiws.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINCFG", name = "reportingtxstates")
@NamedQueries({
	@NamedQuery(name = "TransactionStateEntity.findAll", query = "SELECT s FROM TransactionStateEntity s")
	})
public class TransactionStateEntity {

	@Id
	private String status;

	public String getStatus() {
		return status;
	}
}
