package com.nic.service.captcha;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonCreator;

@RestController("captchaRestApiController")
@RequestMapping("/api/captcha/")
public class RestApiController {

	static class CaptchaValue {

		@NotBlank(message = "Please provide captcha value")
		private String captchaValue;

		@JsonCreator
		public CaptchaValue(String captchaValue) {
			this.captchaValue = captchaValue;
		}

		public String getCaptchaValue() {
			return captchaValue;
		}

	}

	static final String sessionHolder = "captchaSessionHolder";

	private final HttpSession session;

	public RestApiController(HttpSession session) {
		this.session = session;
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String[]> handleError(MethodArgumentNotValidException ex) {
		Map<String, String[]> errorMap = new HashMap<>(ex.getBindingResult().getErrorCount());
		errorMap.put("errors", new String[] { ex.getBindingResult().getFieldError().getDefaultMessage() });
		return errorMap;
	}

	@GetMapping("generate")
	public void generateCaptcha(HttpServletResponse response) {
		String captchaText = Captcha.generateText();

		byte[] captchaImage = Captcha.generateImage(captchaText);
		session.setAttribute(sessionHolder, captchaText);

		response.setContentLength(captchaImage.length);
		response.setContentType("image/png");

		try (OutputStream outputStream = response.getOutputStream()) {
			outputStream.write(captchaImage);
			outputStream.flush();
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("validate")
	public String validateCaptcha(@Valid @RequestBody CaptchaValue body) {
		boolean result = StringUtils.equals((String) session.getAttribute(sessionHolder), body.getCaptchaValue());

		if (result)
			return "Captcha is valid";
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Captcha is not valid");
	}

}
