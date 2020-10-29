package com.nic.service;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("prod")
class ErrorHandlerController implements ErrorController {

	@RequestMapping("/error")
	public String handleError() {
		return "forward:/index.html";
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}
