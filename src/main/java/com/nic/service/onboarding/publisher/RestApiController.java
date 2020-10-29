package com.nic.service.onboarding.publisher;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RestController("onBoardingRestApiController")
@RequestMapping("/api/")
public class RestApiController {

	private final PublisherService service;

	public RestApiController(PublisherService service) {
		this.service = service;
	}

	@PostMapping("onboard/publisher/nic")
	public ResponseEntity<ResponseStatus> processRequestForNic(@RequestBody NicApplicationFormModel requestForm) {
		String referenceNo = service.saveNicApplication(requestForm);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStatus(referenceNo));
	}

	@PostMapping("onboard/publisher/govt")
	public ResponseEntity<ResponseStatus> processRequestForGovt(
			@RequestBody GovernmentApplicationFormModel requestForm) {
		String referenceNo = service.saveGovernmentApplication(requestForm);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStatus(referenceNo));
	}

	@PostMapping("onboard/publisher/other")
	public ResponseEntity<ResponseStatus> processRequestForOthers(@RequestBody OthersApplicationFormModel requestForm) {
		String referenceNo = service.saveOthersApplication(requestForm);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStatus(referenceNo));
	}

	@GetMapping(value = "onboard/verify/does-not-exist", params = "domain-name")
	public Map<String, String> canDomainNameBeUsed(
			@RequestParam("domain-name") @Pattern(regexp = "^([a-z0-9\\.-]*)\\.((nic\\.in)|(gov\\.in))$", message = "Invalid domain name provided!") String domainName) {
		if (service.isDomainNameRegisteredInDatabase(domainName))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Domain '" + domainName + "' already exist!");

		Map<String, String> response = new HashMap<>(1);
		response.put("message", "Domain '" + domainName + "' can be used");
		return response;
	}

}
