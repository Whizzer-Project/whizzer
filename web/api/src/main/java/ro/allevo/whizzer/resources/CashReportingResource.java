package ro.allevo.whizzer.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

public class CashReportingResource {
	
	private EntityManager entityManagerData;
	private UriInfo uriInfo;
	
	public CashReportingResource(UriInfo uriInfo, EntityManager entityManagerData) {
		this.uriInfo = uriInfo;
		this.entityManagerData = entityManagerData;
	}
	@Path("balance-sheet/comparison")
	public BalanceSheetComparisonResources getCashReporting1() {
		return new BalanceSheetComparisonResources(uriInfo, entityManagerData);
	}
	@Path("cash-flow/forecast")
	public CashFlowForecastResources getCashFlowForecast() {
		return new CashFlowForecastResources(uriInfo, entityManagerData);
	}
	@Path("cash-flow/comparison-report")
	public CashFlowComparisonReportResources getCashFlowComparisonReportResources() {
		return new CashFlowComparisonReportResources(uriInfo, entityManagerData);
	}
	@Path("cash-flow/comparison")
	public CashFlowComparisonResources getCashFlowComparisonResources() {
		return new CashFlowComparisonResources(uriInfo, entityManagerData);
	}
	
}
