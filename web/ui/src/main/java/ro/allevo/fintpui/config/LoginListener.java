package ro.allevo.fintpui.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import ro.allevo.fintpui.service.QueueServiceImpl;
import ro.allevo.fintpui.service.ReportService;
import ro.allevo.fintpui.utils.Roles;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired ReportService reportService;

	@Autowired
	private QueueServiceImpl queueService;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		final String BANNER = "banner";
		if (null == request.getSession().getAttribute(BANNER)) {
			request.getSession().setAttribute(BANNER, "45");
		} else {
			request.getSession().setAttribute(BANNER, request.getSession().getAttribute(BANNER));
		}
		request.getSession().setAttribute("isAuthentific", event.getAuthentication().isAuthenticated());
		request.getSession().setAttribute("userName", event.getAuthentication().getName());
		GrantedAuthority returnRole = event.getAuthentication().getAuthorities().stream()
				.filter(role -> role.toString().equals(Roles.QUEUES_VIEW)).findAny().orElse(null);
//		if (null != returnRole)
			request.getSession().setAttribute("listTransactions", queueService.getTransactions("Transactions"));
//		else
//			request.getSession().setAttribute("listTransactions", new Queue[0]);
		returnRole = event.getAuthentication().getAuthorities().stream()
				.filter(role -> role.toString().equals(Roles.EVENTS_VIEW)).findAny().orElse(null);
//		if (null != returnRole)
			request.getSession().setAttribute("listReports", reportService.getReportName());
//		else 
//			request.getSession().setAttribute("listReports", new HashSet<String>());
	}

}
