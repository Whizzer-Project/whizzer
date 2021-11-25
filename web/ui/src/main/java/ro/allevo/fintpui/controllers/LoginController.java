/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
public class LoginController {
	
	@Value("${fintpui.api.auth-url}")
	private String url;
	
//	@Autowired
//	private RestOperations restOperations;
	
	@Autowired
	private HttpServletRequest request;
		
	@RequestMapping(value= {"/login", "/"})
	public String login(ModelMap model) {
		return "redirect:/home?fp=Home";
	}
 
	@RequestMapping(value="/accessdenied")
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "redirect:/home?fp=Home";
	}
 
	@RequestMapping(value= "/logout")
	public String logout(HttpSession session, Model model) {
		
//		//restOperations.delete(url+"/token");
//		auth.eraseCredentials();
//		auth.setAuthenticated(false);
//		authentication.setAuthenticated(false);
//		Enumeration<String> enumString = request.getSession().getAttributeNames();
//		while (enumString.hasMoreElements()) {
//			String attr = enumString.nextElement();
//			request.getSession().removeAttribute(attr);
//		}
//		request.getSession().invalidate();
//		request.getSession().setMaxInactiveInterval(-1000);
//		request.getSession(false);
//		for (Cookie cookie : request.getCookies()) {
//			cookie.setValue(null);
//			cookie.setMaxAge(0);
//		}
//		new SecurityContextLogoutHandler().logout(request, null, null);
		session.invalidate();
    	return "redirect:/";
	}

	@RequestMapping(value = "/pin")
	@ResponseStatus(value = HttpStatus.OK)
	public void pin(@RequestParam(value = "pin", required = true) String pin) {
		request.getSession().setAttribute("banner", pin);
	}
}
