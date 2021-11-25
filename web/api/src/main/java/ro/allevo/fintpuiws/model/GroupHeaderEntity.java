package ro.allevo.fintpuiws.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GroupHeaderEntity {
	
	private String kfname;
	
	private String kname;
	
	@Id
	private long rnum;

	public String getKfname() {
		return kfname;
	}

	public void setKfname(String kfname) {
		this.kfname = kfname;
	}

	public String getKname() {
		return kname;
	}

	public void setKname(String kname) {
		this.kname = kname;
	}

	public long getRnum() {
		return rnum;
	}

	public void setRnum(long rnum) {
		this.rnum = rnum;
	}
}
