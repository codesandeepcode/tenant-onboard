package com.nic.service.esign;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nic.service.utils.Utils;

@Controller("eSignWebApplicationController")
@RequestMapping("/services")
public class WebController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final EsignService service;

	private final XmlMapper xmlMapper;

	private final Environment env;

	private final String domainNameUrl;

	private final String subDirectoryUrl;

	private final String eSignGatewayUrl;

	private final String reactApplicationUrl;

	public WebController(EsignService service, MappingJackson2XmlHttpMessageConverter xmlConverter,
			Environment environment) {
		this.service = service;
		this.xmlMapper = (XmlMapper) xmlConverter.getObjectMapper();
		this.env = environment;
		this.domainNameUrl = env.getProperty("application.domain-name");
		this.subDirectoryUrl = env.getProperty("application.sub-directory");
		this.eSignGatewayUrl = env.getRequiredProperty("nic-esign.gateway.main-url");
		this.reactApplicationUrl = env.getProperty("react.application-base-url", "");
	}

	// add exception validation constraints for page like type here

	static class ESignRequestBody {

		@NotBlank
		@Pattern(regexp = "^NAPIX\\/[\\d]{5}$")
		private String applicationNo;

		@NotBlank
		@Pattern(regexp = "^[a-zA-Z ]*$")
		private String signingPerson;

		public String getApplicationNo() {
			return applicationNo;
		}

		public void setApplicationNo(String applicationNo) {
			this.applicationNo = applicationNo;
		}

		public String getSigningPerson() {
			return signingPerson;
		}

		public void setSigningPerson(String signingPerson) {
			this.signingPerson = signingPerson;
		}

	}

	@PostMapping(value = "/esign/process/esign-form")
	public String startSigningProcess(@Valid ESignRequestBody body, BindingResult bindingResult,
			HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addAttribute("status", "failed");
			redirectAttributes.addAttribute("error-message",
					"Validation errors encountered! Error count is " + bindingResult.getErrorCount());
			return "redirect:" + reactApplicationUrl + "/esign/process-response";
		}

		String applicationNo = body.getApplicationNo();
		String signingPerson = body.getSigningPerson();
		String errorMessage = "Unknown Exception occured!";

		log.debug("Starting signing process for application with ref no {}, to be signed by {}", applicationNo,
				signingPerson);

		try {
			model.addAttribute("signedXml", service.getSignedRequestInXml(applicationNo, signingPerson, request));
			model.addAttribute("aspRedirectUrl", Utils.returnBaseUrl(request, domainNameUrl, subDirectoryUrl)
					+ "/services/esign/process/gateway-response");
			model.addAttribute("nameOfUser", signingPerson);
			model.addAttribute("eSignGatewayUrl", eSignGatewayUrl);

			return "esign/redirecttogateway";
		} catch (IllegalArgumentException ex) {
			if (StringUtils.containsIgnoreCase(ex.getMessage(), "Invalid application"))
				errorMessage = "Application number " + applicationNo + " is invalid!";
		} catch (Exception e) {
			log.error("Error occured when initiating esigning {}", e);
		}

		redirectAttributes.addAttribute("status", "failed");
		redirectAttributes.addAttribute("error-message", errorMessage);
		redirectAttributes.addAttribute("application-no", applicationNo);

		return "redirect:" + reactApplicationUrl + "/esign/process-response";
	}

	@RequestMapping(value = "/esign/process/gateway-response")
	public String receiveEsignGatewayResponse(@RequestParam(name = "respon", required = false) String response,
			RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(response)) {
			redirectAttributes.addAttribute("status", "failed");
			redirectAttributes.addAttribute("error-message", "No response from e-sign service provided!");

			return "redirect:" + reactApplicationUrl + "/esign/process-response";
		}

		log.debug("E-Sign Gateway response (in xml) - {}", response);

		try {
			ESignResponseModel responseModel = xmlMapper.readValue(response, ESignResponseModel.class);
			String applicationNo = service.processESignResponse(responseModel);

			if (responseModel.getStatus().equals("1"))
				redirectAttributes.addAttribute("status", "success");
			else {
				redirectAttributes.addAttribute("status", "failed");
				redirectAttributes.addAttribute("error-message", responseModel.getErrorMessage());
			}

			redirectAttributes.addAttribute("application-no", applicationNo);
		} catch (Exception e) {
			log.error("Exception caused : {}", e);
			log.error("The response from esign gateway that caused exception : {}", response);

			redirectAttributes.addAttribute("status", "failed");
			redirectAttributes.addAttribute("error-message", "Unknown Exception occured!");
		}

		return "redirect:" + reactApplicationUrl + "/esign/process-response";
	}

}
