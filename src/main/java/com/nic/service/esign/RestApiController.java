package com.nic.service.esign;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RestController("eSignRestApiController")
public class RestApiController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final EsignService service;

	public RestApiController(EsignService service, Environment env) {
		this.service = service;
	}

	@GetMapping(value = "/api/esign/check-validity")
	public Map<String, String> isProvidedApplicationNumberValid(
			@RequestParam("application-number") @NotBlank(message = "Application number must be provided") @Pattern(regexp = "^NAPIX\\/[\\d]{5}$", message = "Application number's format is incorrect") String applicatioNo) {
		log.debug("Check validity of application no {}", applicatioNo);

		Optional<String> result = service.isApplicationRefNumberValid(applicatioNo);
		if (result.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.get());

		Map<String, String> message = new HashMap<>(1);
		message.put("result", "Application no " + applicatioNo + " is valid");
		return message;
	}

	@GetMapping(value = "/services/download-application-form")
	public void downloadPdfApplicationForm(
			@RequestParam("application-number") @NotBlank(message = "Application number must be provided") @Pattern(regexp = "^NAPIX\\/[\\d]{5}$", message = "Application number's format is incorrect") String applicationNo,
			@RequestParam(defaultValue = "false") boolean signed, HttpServletResponse response) throws IOException {

		log.debug("User opt to get signed pdf report - {}", signed);

		byte[] applicationPdf;
		String fileName;

		try {
			if (signed) {
				applicationPdf = service.getSignedPdfReport(applicationNo);
				fileName = "Application Ref No (Signed) " + applicationNo;
			} else {
				applicationPdf = service.getUnsignedPdfReport(applicationNo);
				fileName = "Application Ref No " + applicationNo;
			}
		} catch (Exception e) {
			log.error("Error when generating signed pdf - {}", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		response.setContentLength(applicationPdf.length);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", fileName));

		try (OutputStream outStream = response.getOutputStream()) {
			outStream.write(applicationPdf);
			outStream.flush();
		}
	}

}
