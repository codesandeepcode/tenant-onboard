package com.nic.service.employeedetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Service
@ConditionalOnProperty(value = "run.with.external-service-api", havingValue = "true")
class ExternalApiServiceImpl implements ExternalApiService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final HttpEntity<HttpHeaders> tokenEntity;

	private final RestTemplate restTemplate;

	private final ObjectWriter writer;

	private String tokenHolder = null;

	ExternalApiServiceImpl(ObjectMapper mapper, RestTemplateBuilder restBuilder,
			Jackson2ObjectMapperBuilder mapperBuilder) {

		this.restTemplate = restBuilder.messageConverters(
				new MappingJackson2HttpMessageConverter(
						mapperBuilder.build().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)),
				new MappingJackson2XmlHttpMessageConverter(mapperBuilder.createXmlMapper(true).build()
						.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)))
				.build();

		this.writer = mapper.writer();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION,
				"Basic eDdDRTFLdXZwZlRSUTUxMHc1YlFfRXJaNEJBYTp3dWtGSmpJcTFLcWpBVHpBaUhkZjVtbVE2TUVh");
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		log.debug("Headers set - {}", headers);
		tokenEntity = new HttpEntity<>(headers);
	}

	// TODO: please provide the url for bharatapi via constructor
	private String generateToken() {
		AccessTokenModel model = restTemplate.postForObject(
				"https://igw.bharatapi.gov.in/token?grant_type=client_credentials", tokenEntity,
				AccessTokenModel.class);

		if (log.isDebugEnabled()) {
			try {
				log.debug("Access Toke Model Generated - {}", writer.writeValueAsString(model));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return model.getAccessToken();
	}

	// TODO: please provide the url for bharatapi via constructor
	private EmployeeDetail makeApiCall(Integer empCode) {
		if (tokenHolder == null)
			tokenHolder = generateToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(tokenHolder);

		return restTemplate.exchange(
				"https://igw.bharatapi.gov.in/t/digitalapi.nic.in/emd/v1/emdmaster.php?var={employee_code}&processid=EmployeeMaster",
				HttpMethod.GET, new HttpEntity<>(headers), EmployeeWrapper.class, empCode).getBody()
				.getEmployeeDetails();
	}

	@Override
	public EmployeeDetail fetchEmployeeDetails(Integer employeeCode) {
		log.debug("Fetching employee details with given employee code {}", employeeCode);

		EmployeeDetail empDetails;

		try {
			empDetails = makeApiCall(employeeCode);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				tokenHolder = generateToken();
				empDetails = makeApiCall(employeeCode);
			} else
				throw new RuntimeException("Client-side exception", e);
		}

		if (log.isDebugEnabled()) {
			try {
				log.debug("Details of Employee for given '{}' code - {}", employeeCode,
						writer.writeValueAsString(empDetails));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return empDetails;
	}

}
