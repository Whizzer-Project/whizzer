package ro.allevo.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@GetMapping(value = "/login")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView();
		if (null != error) {
			model.addObject("error", "Invalid username and password");
		}
		if (null != logout) {
			model.addObject("msg", "You've been logged out successfully");
		}
		model.addObject("title", "Login");
		model.setViewName("login");
		return model;
	}
}
