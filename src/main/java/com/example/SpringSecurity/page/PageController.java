package com.example.SpringSecurity.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/home")
	public String homepage(){
		return "MainPage";
	}
}
