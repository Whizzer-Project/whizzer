package ro.allevo.whizzer.resources;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@Path("/whizzer-api")
@Singleton
@Repository
@ComponentScan
public class WhizzerApiResourse {
	
	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpServletRequest request;
	
	@PersistenceContext(unitName="fintpCFG")
	public EntityManager emc;
	
	@PersistenceContext(unitName="fintpDATA")
	public EntityManager emd;
	
	@Path("balance-sheet")
	public BalanceSheetsResource getBalanceSheetLoad() {
		return new BalanceSheetsResource(uriInfo, emd);
	}
	
	@Path("profit-loss")
	public ProfitLossesResource getProfitLossLoad() {
		return new ProfitLossesResource(uriInfo, emd);
	}
	
	@Path("profit-loss-report")
	public ProfitLossReportResource getProfitLossReport() {
		return new ProfitLossReportResource(uriInfo, emd);
	}
	
	@Path("balance-sheet-report")
	public BalanceSheetReportResource getBalanceSheetReport() {
		return new BalanceSheetReportResource(uriInfo, emd);
	}
	
	@Path("balance-sheet-forecast")
	public BalanceSheetForecastResources getBalanceSheetForecastResources() {
		return new BalanceSheetForecastResources(uriInfo, emd);
	}
	
	@Path("cash-reporting")
	public CashReportingResource getCashReporting() {
		return new CashReportingResource(uriInfo, emd);
	}
	
	@Path("config")
	public ConfigResource getConfigResource() {
		return new ConfigResource(uriInfo, emc, emd);
	}

}
