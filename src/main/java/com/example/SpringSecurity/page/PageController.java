package com.example.SpringSecurity.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PageController {

	@GetMapping("/home")
	public String homePage(){
		return "Home";
	}

	@GetMapping("/profile")
	public String profilePage(Principal principal, Model model){
		String username = principal.getName();
		model.addAttribute("username", username);
		return "Profile";
	}

	@GetMapping("/login")
	public String loginPage(){
		return "Login";
	}

	@GetMapping("/register")
	public String registerPage(){
		return "Register";
	}

}
