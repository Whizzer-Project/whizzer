package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.RepreconInvVsStatement;

@Service
public interface RepreconInvSuppVsStatementDao {
	
	public RepreconInvVsStatement[] getAllRepreconInvSuppVsStatement();
	public RepreconInvVsStatement getRepreconInvSuppVsStatement(String id);

}

