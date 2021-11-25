package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.RepreconInvVsStatement;

@Service
public interface RepreconInvIssVsStatementDao {
	
	public RepreconInvVsStatement[] getAllRepreconInvIssVsStatement();
	public RepreconInvVsStatement getRepreconInvIssVsStatement(String id);

}

